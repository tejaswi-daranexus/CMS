package com.cms.auth.service.impl;

import com.cms.auth.dto.CreateUserRequest;
import com.cms.auth.dto.LoginRequest;
import com.cms.auth.dto.LoginResponse;
import com.cms.auth.dto.UserResponse;
import com.cms.auth.entity.User;
import com.cms.auth.entity.RefreshToken;
import com.cms.auth.repository.RefreshTokenRepository;
import com.cms.auth.repository.UserRepository;
import com.cms.auth.service.UserService;
import com.cms.common.enums.UserStatus;


import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.cms.auth.security.JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    // ==========================
    // ✅ CREATE USER
    // ==========================
    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);

        // 🔐 HASH PASSWORD
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // optional defaults
        user.setLoginAttempts(0);

        User saved = userRepository.save(user);

        return map(saved);
    }

    // ==========================
    // ✅ LOGIN METHOD (ADDED)
    // ==========================
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // check status
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("User is not active");
        }

        // 🔐 CHECK PASSWORD
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            // increment attempts
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            user.setLastFailedAttemptAt(LocalDateTime.now());
            userRepository.save(user);

            throw new RuntimeException("Invalid email or password");
        }

        // reset attempts on success
        user.setLoginAttempts(0);
        user.setLastFailedAttemptAt(null);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        // ✅ create refresh token
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(refreshToken);

        // ✅ return both tokens
        return new LoginResponse(
                token,
                refreshTokenValue,
                user.getId().toString(),
                user.getRole()
        );
    }

    // ==========================
    // ✅ GET USER
    // ==========================
    @Override
    public UserResponse getUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return map(user);
    }

    // ==========================
    // 🔁 MAPPER
    // ==========================
    private UserResponse map(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setStatus(user.getStatus());
        return res;
    }

    @Override
    public LoginResponse refresh(String refreshTokenValue) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.isRevoked() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateToken(user);

        return new LoginResponse(
                newAccessToken,
                refreshTokenValue,
                user.getId().toString(),
                user.getRole()
        );
    }

    @Override
    public void logout(String refreshTokenValue) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
}
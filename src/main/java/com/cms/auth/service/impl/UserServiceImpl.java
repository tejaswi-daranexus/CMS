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
import com.cms.auth.util.PasswordGenerator;
import com.cms.common.enums.UserStatus;
import com.cms.common.enums.Role;


import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

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

        // 🚫 BLOCK STUDENT AFTER 3 ATTEMPTS
        if (user.getRole() == com.cms.common.enums.Role.STUDENT &&
                user.getLoginAttempts() >= 3) {

            throw new RuntimeException("Account locked. Contact admin.");
        }

        // 🔐 CHECK PASSWORD
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            int attempts = user.getLoginAttempts() + 1;
            user.setLoginAttempts(attempts);
            user.setLastFailedAttemptAt(LocalDateTime.now());
            userRepository.save(user);

            if (user.getRole() == com.cms.common.enums.Role.STUDENT && attempts >= 3) {
                throw new RuntimeException("Account locked after 3 failed attempts. Contact admin.");
            }

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

    @Override
    public void resetStudentAttempts(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("Only students can be reset");
        }

        // 🔥 ADD THIS VALIDATION
        if (user.getLoginAttempts() != 3) {
            throw new RuntimeException("User is not locked (attempts != 3)");
        }

        user.setLoginAttempts(0);
        user.setLastFailedAttemptAt(null);

        userRepository.save(user);
    }

    @Override
    public String regenerateStudentPassword(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("Only students allowed");
        }

        if (user.getLoginAttempts() != 3) {
            throw new RuntimeException("User is not locked (attempts != 3)");
        }

        String plainPassword = PasswordGenerator.generate();

        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setLoginAttempts(0);
        user.setLastFailedAttemptAt(null);

        userRepository.save(user);

        // 🔥 TEMP — return password (later SMS)
        return plainPassword;
    }

    @Override
    public List<String> resetStudentAttemptsBulk(List<UUID> userIds) {

        List<User> users = userRepository.findAllById(userIds);

        List<String> processed = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        for (User user : users) {

            if (user.getRole() != Role.STUDENT) {
                skipped.add(user.getEmail() + " (not a student)");
                continue;
            }

            if (user.getLoginAttempts() != 3) {
                skipped.add(user.getEmail() + " (attempts != 3)");
                continue;
            }

            user.setLoginAttempts(0);
            user.setLastFailedAttemptAt(null);
            processed.add(user.getEmail());
        }

        userRepository.saveAll(users);

        return List.of(
                "Processed: " + processed,
                "Skipped: " + skipped
        );
    }

    @Override
    public List<String> regenerateStudentPasswordBulk(List<UUID> userIds) {

        List<User> users = userRepository.findAllById(userIds);

        List<String> processed = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        for (User user : users) {

            if (user.getRole() != Role.STUDENT) {
                skipped.add(user.getEmail() + " (not a student)");
                continue;
            }

            if (user.getLoginAttempts() != 3) {
                skipped.add(user.getEmail() + " (attempts != 3)");
                continue;
            }

            String plainPassword = PasswordGenerator.generate();

            user.setPassword(passwordEncoder.encode(plainPassword));
            user.setLoginAttempts(0);
            user.setLastFailedAttemptAt(null);

            processed.add(user.getEmail() + " : " + plainPassword);
        }

        userRepository.saveAll(users);

        List<String> response = new ArrayList<>();
        response.add("Processed: " + processed);
        response.add("Skipped: " + skipped);

        return response;
    }
}
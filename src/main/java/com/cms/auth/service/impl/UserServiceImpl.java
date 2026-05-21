package com.cms.auth.service.impl;

import com.cms.auth.dto.CreateAdminRequest;
import com.cms.auth.dto.CreateFacultyRequest;
import com.cms.auth.dto.CreateStudentRequest;
import com.cms.auth.dto.CreateUserRequest;
import com.cms.auth.dto.LoginRequest;
import com.cms.auth.dto.LoginResponse;
import com.cms.auth.dto.UserResponse;
import com.cms.auth.entity.RefreshToken;
import com.cms.auth.entity.User;
import com.cms.auth.repository.RefreshTokenRepository;
import com.cms.auth.repository.UserRepository;
import com.cms.auth.security.JwtService;
import com.cms.auth.service.MailService;
import com.cms.auth.service.UserService;
import com.cms.auth.util.PasswordGenerator;
import com.cms.common.enums.Role;
import com.cms.common.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLoginAttempts(0);

        User saved = userRepository.save(user);

        return map(saved);
    }

    @Override
    public UserResponse createAdmin(CreateAdminRequest request) {

        validateUserCreation(request.getUsername(), request.getEmail());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(Role.ADMIN);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLoginAttempts(0);

        User saved = userRepository.save(user);

        return map(saved);
    }

    @Override
    public UserResponse createFaculty(CreateFacultyRequest request) {

        validateUserCreation(request.getUsername(), request.getEmail());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(Role.FACULTY);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLoginAttempts(0);

        User saved = userRepository.save(user);

        return map(saved);
    }

    @Override
    public UserResponse createStudent(CreateStudentRequest request) {

        validateUserCreation(request.getUsername(), request.getEmail());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(Role.STUDENT);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLoginAttempts(0);

        User saved = userRepository.save(user);

        return map(saved);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("User is not active");
        }

        if (user.getRole() == Role.STUDENT && user.getLoginAttempts() >= 3) {
            throw new RuntimeException("Account locked. Contact admin.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            int attempts = user.getLoginAttempts() + 1;

            user.setLoginAttempts(attempts);
            user.setLastFailedAttemptAt(LocalDateTime.now());

            userRepository.save(user);

            if (user.getRole() == Role.STUDENT && attempts >= 3) {
                throw new RuntimeException("Account locked after 3 failed attempts");
            }

            throw new RuntimeException("Invalid username or password");
        }

        user.setLoginAttempts(0);
        user.setLastFailedAttemptAt(null);
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);

        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(
                accessToken,
                refreshTokenValue,
                user.getId().toString(),
                user.getRole()
        );
    }

    @Override
    public LoginResponse refresh(String refreshTokenValue) {

        RefreshToken oldToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (oldToken.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (oldToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        User user = userRepository.findById(oldToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateToken(user);

        String newRefreshTokenValue = UUID.randomUUID().toString();

        RefreshToken newToken = new RefreshToken();
        newToken.setUserId(user.getId());
        newToken.setToken(newRefreshTokenValue);
        newToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        newToken.setRevoked(false);

        refreshTokenRepository.save(newToken);

        return new LoginResponse(
                newAccessToken,
                newRefreshTokenValue,
                user.getId().toString(),
                user.getRole()
        );
    }

    @Override
    public void logout(String refreshTokenValue) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new RuntimeException("Already logged out");
        }

        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    @Override
    public UserResponse getUser(UUID id, Authentication authentication) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return map(user);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::map);
    }

    @Override
    public Page<UserResponse> getAllStudents(Pageable pageable) {
        return userRepository.findByRole(Role.STUDENT, pageable).map(this::map);
    }

    @Override
    public Page<UserResponse> getAllFaculty(Pageable pageable) {
        return userRepository.findByRole(Role.FACULTY, pageable).map(this::map);
    }

    @Override
    public Page<UserResponse> getAllAdmins(Pageable pageable) {
        return userRepository.findByRole(Role.ADMIN, pageable).map(this::map);
    }

    @Override
    public void resetStudentAttempts(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setLoginAttempts(0);
        user.setLastFailedAttemptAt(null);

        userRepository.save(user);
    }

    @Override
    public String regenerateStudentPassword(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String plainPassword = PasswordGenerator.generate();

        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setLoginAttempts(0);
        user.setLastFailedAttemptAt(null);

        userRepository.save(user);

        mailService.sendPasswordEmail(
                user.getEmail(),
                user.getUsername(),
                plainPassword
        );

        return "Password sent successfully";
    }

    @Override
    public List<String> resetStudentAttemptsBulk(List<UUID> userIds) {

        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            user.setLoginAttempts(0);
            user.setLastFailedAttemptAt(null);
        }

        userRepository.saveAll(users);

        return List.of("Student attempts reset successfully");
    }

    @Override
    public List<String> regenerateStudentPasswordBulk(List<UUID> userIds) {

        List<User> users = userRepository.findAllById(userIds);
        List<String> processed = new ArrayList<>();

        for (User user : users) {

            String plainPassword = PasswordGenerator.generate();

            user.setPassword(passwordEncoder.encode(plainPassword));
            user.setLoginAttempts(0);
            user.setLastFailedAttemptAt(null);

            mailService.sendPasswordEmail(
                    user.getEmail(),
                    user.getUsername(),
                    plainPassword
            );

            processed.add(user.getEmail());
        }

        userRepository.saveAll(users);

        return processed;
    }

    private void validateUserCreation(String username, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
    }

    private UserResponse map(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());

        return response;
    }
}
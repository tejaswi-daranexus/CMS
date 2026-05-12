package com.cms.auth.service.impl;

import com.cms.auth.dto.CreateUserRequest;
import com.cms.auth.dto.CreateAdminRequest;
import com.cms.auth.dto.CreateFacultyRequest;
import com.cms.auth.dto.CreateStudentRequest;
import com.cms.auth.dto.LoginRequest;
import com.cms.auth.dto.LoginResponse;
import com.cms.auth.dto.UserResponse;
import com.cms.auth.entity.User;
import com.cms.auth.entity.RefreshToken;
import com.cms.auth.repository.RefreshTokenRepository;
import com.cms.auth.repository.UserRepository;
import com.cms.auth.service.MailService;
import com.cms.auth.service.UserService;
import com.cms.auth.util.PasswordGenerator;
import com.cms.common.enums.UserStatus;
import com.cms.common.enums.Role;


import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

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
    private final MailService mailService;

    // ==========================
    // ✅ CREATE USER
    // ==========================
    @Override
    public UserResponse createUser(CreateUserRequest request) {

        User user = new User();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);

        // 🔐 HASH PASSWORD
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

        // 🔥 BACKEND CONTROLS ROLE
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

        // 🔥 BACKEND CONTROLS ROLE
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

        // 🔥 BACKEND CONTROLS ROLE
        user.setRole(Role.STUDENT);

        user.setStatus(UserStatus.ACTIVE);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setLoginAttempts(0);

        User saved = userRepository.save(user);

        return map(saved);
    }

    // ==========================
    // ✅ LOGIN METHOD (ADDED)
    // ==========================
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

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

            throw new RuntimeException("Invalid username or password");
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
    public UserResponse getUser(UUID id,
                                Authentication authentication) {

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String callerRole =
                authentication.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority();

    /*
        ROLE_SUPER_ADMIN
        ROLE_ADMIN
    */

        // ✅ SUPER_ADMIN can access everyone
        if (callerRole.equals("ROLE_SUPER_ADMIN")) {
            return map(targetUser);
        }

        // ✅ ADMIN restrictions
        if (callerRole.equals("ROLE_ADMIN")) {

            // ❌ admin cannot access admin/super_admin
            if (targetUser.getRole() == Role.ADMIN ||
                    targetUser.getRole() == Role.SUPER_ADMIN) {

                throw new RuntimeException(
                        "Admins cannot access admin accounts"
                );
            }

            return map(targetUser);
        }

        throw new RuntimeException("Access Denied");
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(this::map);
    }

    @Override
    public Page<UserResponse> getAllStudents(Pageable pageable) {

        return userRepository.findByRole(Role.STUDENT, pageable)
                .map(this::map);
    }

    @Override
    public Page<UserResponse> getAllFaculty(Pageable pageable) {

        return userRepository.findByRole(Role.FACULTY, pageable)
                .map(this::map);
    }

    @Override
    public Page<UserResponse> getAllAdmins(Pageable pageable) {

        return userRepository.findByRole(Role.ADMIN, pageable)
                .map(this::map);
    }

    // ==========================
    // 🔁 MAPPER
    // ==========================
    private UserResponse map(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setStatus(user.getStatus());
        return res;
    }

    private void validateUserCreation(String username, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
    }

    @Override
    public LoginResponse refresh(String refreshTokenValue) {

        RefreshToken oldToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // ❌ token revoked or expired
        if (oldToken.isRevoked() || oldToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        // ✅ revoke old token
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        // ✅ get user
        User user = userRepository.findById(oldToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ generate NEW access token
        String newAccessToken = jwtService.generateToken(user);

        // ✅ generate NEW refresh token
        String newRefreshTokenValue = UUID.randomUUID().toString();

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUserId(user.getId());
        newRefreshToken.setToken(newRefreshTokenValue);
        newRefreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(newRefreshToken);

        // ✅ return NEW refresh token
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

        // ✅ generate password
        String plainPassword = PasswordGenerator.generate();

        // ✅ save hashed password
        user.setPassword(passwordEncoder.encode(plainPassword));

        // ✅ reset lock
        user.setLoginAttempts(0);
        user.setLastFailedAttemptAt(null);

        userRepository.save(user);

        // ✅ send mail
        mailService.sendPasswordEmail(
                user.getEmail(),
                user.getUsername(),
                plainPassword
        );

        return "Password sent to registered email";
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

            try {

                // ✅ generate password
                String plainPassword = PasswordGenerator.generate();

                // ✅ save hashed password
                user.setPassword(passwordEncoder.encode(plainPassword));

                // ✅ reset attempts
                user.setLoginAttempts(0);
                user.setLastFailedAttemptAt(null);

                // ✅ send mail
                mailService.sendPasswordEmail(
                        user.getEmail(),
                        user.getUsername(),
                        plainPassword
                );

                processed.add(user.getEmail());

            } catch (Exception e) {

                skipped.add(user.getEmail() + " (mail failed)");
            }
        }

        userRepository.saveAll(users);

        List<String> response = new ArrayList<>();

        response.add("Processed: " + processed);
        response.add("Skipped: " + skipped);

        return response;
    }
}
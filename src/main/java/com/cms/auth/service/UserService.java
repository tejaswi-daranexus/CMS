package com.cms.auth.service;

import com.cms.auth.dto.CreateUserRequest;
import com.cms.auth.dto.UserResponse;
import com.cms.auth.dto.LoginRequest;
import com.cms.auth.dto.LoginResponse;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUser(UUID id);

    LoginResponse login(LoginRequest request);

    // ✅ REFRESH TOKEN
    LoginResponse refresh(String refreshToken);

    // ✅ LOGOUT
    void logout(String refreshToken);

    // ✅ ADD THESE NEW METHODS
    void resetStudentAttempts(UUID userId);

    String regenerateStudentPassword(UUID userId);
}
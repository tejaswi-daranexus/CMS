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

    // ✅ ADD THIS
    LoginResponse refresh(String refreshToken);

    // ✅ ADD THIS (for logout next)
    void logout(String refreshToken);
}
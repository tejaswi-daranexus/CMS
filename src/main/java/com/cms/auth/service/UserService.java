package com.cms.auth.service;

import com.cms.auth.dto.*;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse createAdmin(CreateAdminRequest request);

    UserResponse createFaculty(CreateFacultyRequest request);

    UserResponse createStudent(CreateStudentRequest request);

    UserResponse getUser(UUID id);

    LoginResponse login(LoginRequest request);

    // ✅ ADD THIS
    LoginResponse refresh(String refreshToken);

    // ✅ ADD THIS (for logout next)
    void logout(String refreshToken);
}
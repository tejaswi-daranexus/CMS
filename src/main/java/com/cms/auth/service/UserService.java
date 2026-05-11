package com.cms.auth.service;

import com.cms.auth.dto.*;

import java.util.UUID;
import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse createAdmin(CreateAdminRequest request);

    UserResponse createFaculty(CreateFacultyRequest request);

    UserResponse createStudent(CreateStudentRequest request);

    UserResponse getUser(UUID id);

    LoginResponse login(LoginRequest request);

    // ✅ REFRESH TOKEN
    LoginResponse refresh(String refreshToken);

    // ✅ LOGOUT
    void logout(String refreshToken);

    // ✅ SINGLE OPERATIONS
    void resetStudentAttempts(UUID userId);

    String regenerateStudentPassword(UUID userId);

    // ✅ BULK OPERATIONS (ADD THESE)
    List<String> resetStudentAttemptsBulk(List<UUID> userIds);

    List<String> regenerateStudentPasswordBulk(List<UUID> userIds);
}
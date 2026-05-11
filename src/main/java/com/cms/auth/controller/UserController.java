package com.cms.auth.controller;

import com.cms.auth.dto.CreateUserRequest;
import com.cms.auth.dto.CreateAdminRequest;
import com.cms.auth.dto.CreateFacultyRequest;
import com.cms.auth.dto.CreateStudentRequest;
import com.cms.auth.dto.UserResponse;
import com.cms.common.dto.ApiResponse;
import com.cms.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@RestController
@RequestMapping("/cms/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return new ApiResponse<>(
                true,
                "User created successfully",
                userService.createUser(request)
        );
    }*/

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/admins")
    public ApiResponse<UserResponse> createAdmin(
            @Valid @RequestBody CreateAdminRequest request
    ) {

        return new ApiResponse<>(
                true,
                "Admin created successfully",
                userService.createAdmin(request)
        );
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping("/faculty")
    public ApiResponse<UserResponse> createFaculty(
            @Valid @RequestBody CreateFacultyRequest request
    ) {

        return new ApiResponse<>(
                true,
                "Faculty created successfully",
                userService.createFaculty(request)
        );
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping("/students")
    public ApiResponse<UserResponse> createStudent(
            @Valid @RequestBody CreateStudentRequest request
    ) {

        return new ApiResponse<>(
                true,
                "Student created successfully",
                userService.createStudent(request)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<UserResponse> getUser(@PathVariable UUID id) {
        return new ApiResponse<>(
                true,
                "User fetched successfully",
                userService.getUser(id)
        );
    }
}
package com.cms.auth.controller;

import com.cms.auth.dto.CreateUserRequest;
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

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return new ApiResponse<>(
                true,
                "User created successfully",
                userService.createUser(request)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> getUser(@PathVariable UUID id) {
        return new ApiResponse<>(
                true,
                "User fetched successfully",
                userService.getUser(id)
        );
    }
}
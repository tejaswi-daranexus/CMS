package com.cms.auth.controller;

import com.cms.auth.service.UserService;
import com.cms.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cms/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // 🔁 Reset attempts
    @PostMapping("/students/{userId}/reset-attempts")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> resetAttempts(@PathVariable UUID userId) {

        userService.resetStudentAttempts(userId);

        return new ApiResponse<>(true, "Attempts reset successfully", null);
    }

    // 🔐 Regenerate password
    @PostMapping("/students/{userId}/regenerate-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> regeneratePassword(@PathVariable UUID userId) {

        String newPassword = userService.regenerateStudentPassword(userId);

        return new ApiResponse<>(true, "New password generated", newPassword);
    }
}
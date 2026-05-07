package com.cms.auth.controller;

import com.cms.auth.dto.LoginRequest;
import com.cms.auth.dto.LoginResponse;
import com.cms.auth.service.UserService;
import com.cms.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return new ApiResponse<>(
                true,
                "Login successful",
                userService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@RequestParam String refreshToken) {
        return new ApiResponse<>(
                true,
                "Token refreshed",
                userService.refresh(refreshToken)
        );
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestParam String refreshToken) {
        userService.logout(refreshToken);
        return new ApiResponse<>(true, "Logged out successfully", null);
    }
}
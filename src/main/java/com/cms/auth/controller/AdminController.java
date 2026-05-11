package com.cms.auth.controller;

//import com.cms.auth.service.MailService;

import com.cms.auth.service.UserService;
import com.cms.common.dto.ApiResponse;
import com.cms.auth.dto.BulkUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/cms/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    //private final MailService mailService;

    // 🔁 Reset attempts
    @PostMapping("/students/{userId}/reset-attempts")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<String> resetAttempts(@PathVariable UUID userId) {

        userService.resetStudentAttempts(userId);

        return new ApiResponse<>(true, "Attempts reset successfully", null);
    }

    // 🔐 Regenerate password
    @PostMapping("/students/{userId}/regenerate-password")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<String> regeneratePassword(@PathVariable UUID userId) {

        String newPassword = userService.regenerateStudentPassword(userId);

        return new ApiResponse<>(true, "New password generated", newPassword);
    }

    //🔁 Bulk Reset
    @PostMapping("/students/reset-attempts-bulk")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<List<String>> resetAttemptsBulk(@RequestBody BulkUserRequest request) {

        List<String> result = userService.resetStudentAttemptsBulk(request.getUserIds());

        return new ApiResponse<>(true, "Bulk reset completed", result);
    }

    //🔐 Bulk Regenerate
    @PostMapping("/students/regenerate-password-bulk")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<List<String>> regeneratePasswordBulk(@RequestBody BulkUserRequest request) {

        List<String> passwords = userService.regenerateStudentPasswordBulk(request.getUserIds());

        return new ApiResponse<>(true, "Bulk password regeneration successful", passwords);
    }

    /*
    @PostMapping("/test-mail")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ApiResponse<String> testMail(
            @RequestParam String email
    ) {

        mailService.sendPasswordEmail(
                email,
                "22CSE101",
                "Temp@123"
        );

        return new ApiResponse<>(
                true,
                "Mail sent successfully",
                null
        );
    }*/
}
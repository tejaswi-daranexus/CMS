package com.cms.admin.controller;

import com.cms.admin.dto.AdminDetailResponse;
import com.cms.admin.service.AdminQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminQueryService adminQueryService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<AdminDetailResponse> getMyProfile() {

        return ResponseEntity.ok(
                adminQueryService.getMyProfile()
        );
    }
}
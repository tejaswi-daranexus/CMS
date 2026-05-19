package com.cms.admin.controller;

import com.cms.admin.dto.AdminDetailResponse;
import com.cms.admin.service.AdminQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cms.admin.dto.AdminSummaryResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

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

    @GetMapping
    @PreAuthorize("""
        hasAnyRole(
            'SUPER_ADMIN',
            'ADMIN'
        )
        """)
    public ResponseEntity<Page<AdminSummaryResponse>>
    getAdmins(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                adminQueryService.getAdmins(
                        page,
                        size
                )
        );
    }

    @GetMapping("/{adminId}")
    @PreAuthorize("""
        hasAnyRole(
            'SUPER_ADMIN',
            'ADMIN'
        )
        """)
    public ResponseEntity<AdminDetailResponse>
    getAdminById(

            @PathVariable UUID adminId
    ) {

        return ResponseEntity.ok(
                adminQueryService.getAdminById(
                        adminId
                )
        );
    }
}
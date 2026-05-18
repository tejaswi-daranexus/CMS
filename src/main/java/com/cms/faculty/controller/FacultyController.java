package com.cms.faculty.controller;

import com.cms.faculty.dto.FacultyDetailResponse;
import com.cms.faculty.service.FacultyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/api/v1/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyQueryService facultyQueryService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<FacultyDetailResponse> getMyProfile() {

        return ResponseEntity.ok(
                facultyQueryService.getMyProfile()
        );
    }
}
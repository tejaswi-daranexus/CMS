package com.cms.faculty.controller;

import com.cms.faculty.dto.FacultyDetailResponse;
import com.cms.faculty.service.FacultyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cms.faculty.dto.FacultySummaryResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

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

    @GetMapping
    @PreAuthorize("""
        hasAnyRole(
            'SUPER_ADMIN',
            'ADMIN',
            'FACULTY'
        )
        """)
    public ResponseEntity<Page<FacultySummaryResponse>>
    getFaculty(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                facultyQueryService.getFaculty(
                        page,
                        size
                )
        );
    }


    @GetMapping("/{facultyId}")
    @PreAuthorize("""
        hasAnyRole(
            'SUPER_ADMIN',
            'ADMIN',
            'FACULTY'
        )
        """)
    public ResponseEntity<FacultyDetailResponse>
    getFacultyById(

            @PathVariable UUID facultyId
    ) {

        return ResponseEntity.ok(
                facultyQueryService.getFacultyById(
                        facultyId
                )
        );
    }
}
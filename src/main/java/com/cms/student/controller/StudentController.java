package com.cms.student.controller;

import com.cms.student.dto.StudentDetailResponse;
import com.cms.student.dto.StudentSummaryResponse;
import com.cms.student.service.StudentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

@RestController
@RequestMapping("/cms/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentQueryService studentQueryService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentDetailResponse> getMyProfile() {

        return ResponseEntity.ok(
                studentQueryService.getMyProfile()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'FACULTY')")
    public ResponseEntity<Page<StudentSummaryResponse>> getStudents(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                studentQueryService.getStudents(page, size)
        );
    }


    @GetMapping("/{studentId}")
    @PreAuthorize("""
        hasAnyRole(
            'SUPER_ADMIN',
            'ADMIN',
            'FACULTY',
            'STUDENT'
        )
        """)
    public ResponseEntity<StudentDetailResponse> getStudentById(

            @PathVariable UUID studentId
    ) {

        return ResponseEntity.ok(
                studentQueryService.getStudentById(studentId)
        );
    }
}
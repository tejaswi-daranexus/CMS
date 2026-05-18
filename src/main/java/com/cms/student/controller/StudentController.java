package com.cms.student.controller;

import com.cms.student.dto.StudentDetailResponse;
import com.cms.student.service.StudentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}
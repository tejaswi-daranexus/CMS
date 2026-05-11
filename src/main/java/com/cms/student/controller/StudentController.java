package com.cms.student.controller;

import com.cms.student.dto.StudentCreateRequest;
import com.cms.student.dto.StudentResponse;
import com.cms.student.dto.StudentUpdateRequest;
import com.cms.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(
            @RequestBody StudentCreateRequest request
    ) {
        return ResponseEntity.ok(
                studentService.createStudent(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(
                studentService.getAllStudents()
        );
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable UUID studentId,
            @RequestBody StudentUpdateRequest request
    ) {
        return ResponseEntity.ok(
                studentService.updateStudent(studentId, request)
        );
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable UUID studentId
    ) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok("Student deleted successfully");
    }
}
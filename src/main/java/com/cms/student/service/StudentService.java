package com.cms.student.service;

import com.cms.student.dto.StudentCreateRequest;
import com.cms.student.dto.StudentResponse;
import com.cms.student.dto.StudentUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    StudentResponse createStudent(StudentCreateRequest request);

    List<StudentResponse> getAllStudents();

    StudentResponse updateStudent(UUID studentId, StudentUpdateRequest request);

    void deleteStudent(UUID studentId);
}
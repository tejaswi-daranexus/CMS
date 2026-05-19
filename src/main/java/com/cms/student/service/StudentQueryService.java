package com.cms.student.service;

import com.cms.student.dto.StudentDetailResponse;
import com.cms.student.dto.StudentSummaryResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface StudentQueryService {

    StudentDetailResponse getMyProfile();

    Page<StudentSummaryResponse> getStudents(
            int page,
            int size
    );

    StudentDetailResponse getStudentById(
            UUID studentId
    );
}
package com.cms.faculty.service;

import com.cms.faculty.dto.FacultyDetailResponse;
import com.cms.faculty.dto.FacultySummaryResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface FacultyQueryService {

    FacultyDetailResponse getMyProfile();

    Page<FacultySummaryResponse> getFaculty(
            int page,
            int size
    );


    FacultyDetailResponse getFacultyById(
            UUID facultyId
    );


}
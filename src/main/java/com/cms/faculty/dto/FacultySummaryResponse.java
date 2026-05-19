package com.cms.faculty.dto;

import com.cms.common.enums.FacultyDesignation;
import com.cms.common.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FacultySummaryResponse {

    private UUID userId;

    private String username;

    private String institutionalEmail;

    private String employeeId;

    private String fullName;

    private String phoneNumber;

    private String departmentName;

    private FacultyDesignation designation;

    private Integer experienceYears;

    private UserStatus status;
}
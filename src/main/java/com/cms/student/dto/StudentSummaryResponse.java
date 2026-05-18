package com.cms.student.dto;

import com.cms.common.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class StudentSummaryResponse {

    private UUID userId;

    private String username;

    private String institutionalEmail;

    private String fullName;

    private String rollNumber;

    private String phoneNumber;

    private String sectionName;

    private String branchName;

    private String departmentName;

    private Integer admissionYear;

    private UserStatus status;
}
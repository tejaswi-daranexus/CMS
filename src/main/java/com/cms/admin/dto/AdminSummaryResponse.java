package com.cms.admin.dto;

import com.cms.common.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminSummaryResponse {

    private UUID userId;

    private String username;

    private String institutionalEmail;

    private String employeeId;

    private String fullName;

    private String phoneNumber;

    private String departmentName;

    private String designation;

    private UserStatus status;
}
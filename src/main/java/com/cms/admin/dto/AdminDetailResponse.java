package com.cms.admin.dto;

import com.cms.common.dto.DepartmentMiniResponse;
import com.cms.common.enums.AdminDesignation;
import com.cms.common.enums.BloodGroup;
import com.cms.common.enums.Gender;
import com.cms.common.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AdminDetailResponse {

    // =========================
    // AUTH / IDENTITY
    // =========================

    private UUID userId;

    private String username;

    private String institutionalEmail;

    private UserStatus status;

    // =========================
    // PROFESSIONAL
    // =========================

    private String employeeId;

    private AdminDesignation designation;

    private DepartmentMiniResponse department;

    private LocalDate joiningDate;

    // =========================
    // BASIC PROFILE
    // =========================

    private String fullName;

    private String phoneNumber;

    private String personalEmail;

    private LocalDate dateOfBirth;

    private Gender gender;

    private BloodGroup bloodGroup;

    // =========================
    // PROFILE LINKS
    // =========================

    private String profileImageUrl;

    private String linkedinUrl;

    private String researchPapersUrl;

    // =========================
    // ADDRESS
    // =========================

    private String currentAddress;

    private String permanentAddress;

    // =========================
    // EMERGENCY CONTACT
    // =========================

    private String emergencyContactName;

    private String emergencyContactPhone;
}
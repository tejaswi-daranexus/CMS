package com.cms.faculty.dto;

import com.cms.common.dto.DepartmentMiniResponse;
import com.cms.common.enums.BloodGroup;
import com.cms.common.enums.FacultyDesignation;
import com.cms.common.enums.Gender;
import com.cms.common.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class FacultyDetailResponse {

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

    private FacultyDesignation designation;

    private DepartmentMiniResponse department;

    private String qualification;

    private String specialization;

    private Integer experienceYears;

    private LocalDate joiningDate;

    private String officeLocation;

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
    // RESEARCH
    // =========================

    private String researchInterests;

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
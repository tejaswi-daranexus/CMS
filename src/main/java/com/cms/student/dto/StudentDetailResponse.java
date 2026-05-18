package com.cms.student.dto;

import com.cms.common.dto.SectionMiniResponse;
import com.cms.common.enums.BloodGroup;
import com.cms.common.enums.Category;
import com.cms.common.enums.Gender;
import com.cms.common.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class StudentDetailResponse {

    // =========================
    // AUTH / IDENTITY
    // =========================

    private UUID userId;

    private String username;

    private String institutionalEmail;

    private UserStatus status;

    // =========================
    // ACADEMIC
    // =========================

    private String rollNumber;

    private Integer admissionYear;

    private SectionMiniResponse section;

    // =========================
    // BASIC PROFILE
    // =========================

    private String fullName;

    private String phoneNumber;

    private String personalEmail;

    private LocalDate dateOfBirth;

    private Gender gender;

    private BloodGroup bloodGroup;

    private Category category;

    // =========================
    // PROFILE LINKS
    // =========================

    private String profileImageUrl;

    private String linkedinUrl;

    private String githubUrl;

    // =========================
    // ADDRESS
    // =========================

    private String currentAddress;

    private String permanentAddress;

    // =========================
    // GUARDIAN DETAILS
    // =========================

    private String guardianName;

    private String guardianRelation;

    private String guardianPhoneNumber;

    private String fatherName;

    private String fatherPhoneNumber;

    private String motherName;

    private String motherPhoneNumber;

    // =========================
    // EMERGENCY CONTACT
    // =========================

    private String emergencyContactName;

    private String emergencyContactPhone;

    // =========================
    // FLAGS
    // =========================

    private boolean hosteller;

    private boolean profileCompleted;
}
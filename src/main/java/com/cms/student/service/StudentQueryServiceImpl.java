package com.cms.student.service;

import com.cms.common.dto.BranchMiniResponse;
import com.cms.common.dto.DepartmentMiniResponse;
import com.cms.common.dto.SectionMiniResponse;
import com.cms.common.security.SecurityUtil;
import com.cms.student.dto.StudentDetailResponse;
import com.cms.student.entity.Student;
import com.cms.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentQueryServiceImpl implements StudentQueryService {

    private final StudentRepository studentRepository;

    @Override
    public StudentDetailResponse getMyProfile() {

        Student student = studentRepository.findById(
                        SecurityUtil.getCurrentUserId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return StudentDetailResponse.builder()

                // =========================
                // AUTH / IDENTITY
                // =========================

                .userId(student.getUser().getId())
                .username(student.getUser().getUsername())
                .institutionalEmail(student.getUser().getEmail())
                .status(student.getUser().getStatus())

                // =========================
                // ACADEMIC
                // =========================

                .rollNumber(student.getRollNumber())
                .admissionYear(student.getAdmissionYear())

                .section(
                        SectionMiniResponse.builder()
                                .id(student.getSection().getId())
                                .name(student.getSection().getName())

                                .branch(
                                        BranchMiniResponse.builder()
                                                .id(student.getSection()
                                                        .getBranch()
                                                        .getId())

                                                .name(student.getSection()
                                                        .getBranch()
                                                        .getName())

                                                .code(student.getSection()
                                                        .getBranch()
                                                        .getCode())

                                                .department(
                                                        DepartmentMiniResponse.builder()
                                                                .id(student.getSection()
                                                                        .getBranch()
                                                                        .getDepartment()
                                                                        .getId())

                                                                .name(student.getSection()
                                                                        .getBranch()
                                                                        .getDepartment()
                                                                        .getName())

                                                                .code(student.getSection()
                                                                        .getBranch()
                                                                        .getDepartment()
                                                                        .getCode())

                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )

                // =========================
                // BASIC PROFILE
                // =========================

                .fullName(student.getFullName())
                .phoneNumber(student.getPhoneNumber())
                .personalEmail(student.getPersonalEmail())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .bloodGroup(student.getBloodGroup())
                .category(student.getCategory())

                // =========================
                // PROFILE LINKS
                // =========================

                .profileImageUrl(student.getProfileImageUrl())
                .linkedinUrl(student.getLinkedinUrl())
                .githubUrl(student.getGithubUrl())

                // =========================
                // ADDRESS
                // =========================

                .currentAddress(student.getCurrentAddress())
                .permanentAddress(student.getPermanentAddress())

                // =========================
                // GUARDIAN DETAILS
                // =========================

                .guardianName(student.getGuardianName())
                .guardianRelation(student.getGuardianRelation())
                .guardianPhoneNumber(student.getGuardianPhoneNumber())

                .fatherName(student.getFatherName())
                .fatherPhoneNumber(student.getFatherPhoneNumber())

                .motherName(student.getMotherName())
                .motherPhoneNumber(student.getMotherPhoneNumber())

                // =========================
                // EMERGENCY CONTACT
                // =========================

                .emergencyContactName(student.getEmergencyContactName())
                .emergencyContactPhone(student.getEmergencyContactPhone())

                // =========================
                // FLAGS
                // =========================

                .hosteller(student.isHosteller())
                .profileCompleted(student.isProfileCompleted())

                .build();
    }
}
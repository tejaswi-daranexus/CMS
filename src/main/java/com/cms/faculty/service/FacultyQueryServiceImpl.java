package com.cms.faculty.service;

import com.cms.common.dto.DepartmentMiniResponse;
import com.cms.common.security.SecurityUtil;
import com.cms.faculty.dto.FacultyDetailResponse;
import com.cms.faculty.entity.Faculty;
import com.cms.faculty.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacultyQueryServiceImpl implements FacultyQueryService {

    private final FacultyRepository facultyRepository;

    @Override
    @Transactional(readOnly = true)
    public FacultyDetailResponse getMyProfile() {

        Faculty faculty = facultyRepository.findById(
                        SecurityUtil.getCurrentUserId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Faculty not found"));

        return FacultyDetailResponse.builder()

                // =========================
                // AUTH / IDENTITY
                // =========================

                .userId(faculty.getUser().getId())
                .username(faculty.getUser().getUsername())
                .institutionalEmail(faculty.getUser().getEmail())
                .status(faculty.getUser().getStatus())

                // =========================
                // PROFESSIONAL
                // =========================

                .employeeId(faculty.getEmployeeId())
                .designation(faculty.getDesignation())

                .department(
                        DepartmentMiniResponse.builder()
                                .id(faculty.getDepartment().getId())
                                .name(faculty.getDepartment().getName())
                                .code(faculty.getDepartment().getCode())
                                .build()
                )

                .qualification(faculty.getQualification())
                .specialization(faculty.getSpecialization())
                .experienceYears(faculty.getExperienceYears())
                .joiningDate(faculty.getJoiningDate())
                .officeLocation(faculty.getOfficeLocation())

                // =========================
                // BASIC PROFILE
                // =========================

                .fullName(faculty.getFullName())
                .phoneNumber(faculty.getPhoneNumber())
                .personalEmail(faculty.getPersonalEmail())
                .dateOfBirth(faculty.getDateOfBirth())
                .gender(faculty.getGender())
                .bloodGroup(faculty.getBloodGroup())

                // =========================
                // PROFILE LINKS
                // =========================

                .profileImageUrl(faculty.getProfileImageUrl())
                .linkedinUrl(faculty.getLinkedinUrl())
                .researchPapersUrl(faculty.getResearchPapersUrl())

                // =========================
                // RESEARCH
                // =========================

                .researchInterests(faculty.getResearchInterests())

                // =========================
                // ADDRESS
                // =========================

                .currentAddress(faculty.getCurrentAddress())
                .permanentAddress(faculty.getPermanentAddress())

                // =========================
                // EMERGENCY CONTACT
                // =========================

                .emergencyContactName(faculty.getEmergencyContactName())
                .emergencyContactPhone(faculty.getEmergencyContactPhone())

                .build();
    }
}
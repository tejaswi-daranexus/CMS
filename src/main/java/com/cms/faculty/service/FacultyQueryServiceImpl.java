package com.cms.faculty.service;

import com.cms.common.dto.DepartmentMiniResponse;
import com.cms.common.security.SecurityUtil;
import com.cms.faculty.dto.FacultyDetailResponse;
import com.cms.faculty.entity.Faculty;
import com.cms.faculty.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cms.admin.entity.Admin;
import com.cms.admin.repository.AdminRepository;
import com.cms.common.enums.Role;
import com.cms.faculty.dto.FacultySummaryResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.cms.admin.entity.Admin;
import com.cms.common.enums.Role;
import com.cms.common.exception.ForbiddenException;
import com.cms.common.exception.ResourceNotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacultyQueryServiceImpl implements FacultyQueryService {

    private final FacultyRepository facultyRepository;

    private final AdminRepository adminRepository;

    @Override
    @Transactional(readOnly = true)
    public FacultyDetailResponse getMyProfile() {

        Faculty faculty = facultyRepository.findById(
                        SecurityUtil.getCurrentUserId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Faculty not found"));

        return mapToFacultyDetailResponse(faculty);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacultySummaryResponse> getFaculty(
            int page,
            int size
    ) {

        Role role = SecurityUtil.getCurrentUserRole();

        UUID currentUserId = SecurityUtil.getCurrentUserId();

        Pageable pageable = PageRequest.of(page, size);

        Page<Faculty> facultyPage;

        switch (role) {

            case SUPER_ADMIN -> {

                facultyPage = facultyRepository.findAllBy(
                        pageable
                );
            }

            case ADMIN -> {

                Admin admin = adminRepository.findById(
                                currentUserId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admin not found"
                                ));

                facultyPage =
                        facultyRepository.findAllByDepartmentId(
                                admin.getDepartment().getId(),
                                pageable
                        );
            }

            case FACULTY -> {

                facultyPage = facultyRepository.findByUserId(
                        currentUserId,
                        pageable
                );
            }

            default -> throw new RuntimeException(
                    "You are not allowed to access faculty"
            );
        }

        return facultyPage.map(faculty ->

                FacultySummaryResponse.builder()

                        .userId(faculty.getUser().getId())

                        .username(
                                faculty.getUser().getUsername()
                        )

                        .institutionalEmail(
                                faculty.getUser().getEmail()
                        )

                        .employeeId(
                                faculty.getEmployeeId()
                        )

                        .fullName(
                                faculty.getFullName()
                        )

                        .phoneNumber(
                                faculty.getPhoneNumber()
                        )

                        .departmentName(
                                faculty.getDepartment().getName()
                        )

                        .designation(
                                faculty.getDesignation()
                        )

                        .experienceYears(
                                faculty.getExperienceYears()
                        )

                        .status(
                                faculty.getUser().getStatus()
                        )

                        .build()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public FacultyDetailResponse getFacultyById(
            UUID facultyId
    ) {

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Faculty not found"
                        ));

        validateFacultyAccess(faculty);

        return mapToFacultyDetailResponse(faculty);
    }



    private void validateFacultyAccess(
            Faculty faculty
    ) {

        Role role = SecurityUtil.getCurrentUserRole();

        UUID currentUserId = SecurityUtil.getCurrentUserId();

        switch (role) {

            case SUPER_ADMIN -> {

                return;
            }

            case ADMIN -> {

                Admin admin = adminRepository.findById(currentUserId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Admin not found"
                                ));

                UUID adminDepartmentId =
                        admin.getDepartment().getId();

                UUID facultyDepartmentId =
                        faculty.getDepartment().getId();

                if (!adminDepartmentId.equals(
                        facultyDepartmentId
                )) {

                    throw new ForbiddenException(
                            "You cannot access this faculty"
                    );
                }
            }

            case FACULTY -> {

                if (!faculty.getUserId()
                        .equals(currentUserId)) {

                    throw new ForbiddenException(
                            "You cannot access this faculty"
                    );
                }
            }

            default -> throw new ForbiddenException(
                    "Access denied"
            );
        }
    }


    private FacultyDetailResponse
    mapToFacultyDetailResponse(
            Faculty faculty
    ) {

        return FacultyDetailResponse.builder()

                // =========================
                // AUTH / IDENTITY
                // =========================

                .userId(faculty.getUser().getId())

                .username(
                        faculty.getUser().getUsername()
                )

                .institutionalEmail(
                        faculty.getUser().getEmail()
                )

                .status(
                        faculty.getUser().getStatus()
                )

                // =========================
                // PROFESSIONAL
                // =========================

                .employeeId(
                        faculty.getEmployeeId()
                )

                .designation(
                        faculty.getDesignation()
                )

                .department(
                        DepartmentMiniResponse.builder()

                                .id(faculty.getDepartment().getId())

                                .name(faculty.getDepartment().getName())

                                .code(faculty.getDepartment().getCode())

                                .build()
                )

                .qualification(
                        faculty.getQualification()
                )

                .specialization(
                        faculty.getSpecialization()
                )

                .experienceYears(
                        faculty.getExperienceYears()
                )

                .joiningDate(
                        faculty.getJoiningDate()
                )

                .officeLocation(
                        faculty.getOfficeLocation()
                )

                // =========================
                // BASIC PROFILE
                // =========================

                .fullName(
                        faculty.getFullName()
                )

                .phoneNumber(
                        faculty.getPhoneNumber()
                )

                .personalEmail(
                        faculty.getPersonalEmail()
                )

                .dateOfBirth(
                        faculty.getDateOfBirth()
                )

                .gender(
                        faculty.getGender()
                )

                .bloodGroup(
                        faculty.getBloodGroup()
                )

                // =========================
                // PROFILE LINKS
                // =========================

                .profileImageUrl(
                        faculty.getProfileImageUrl()
                )

                .linkedinUrl(
                        faculty.getLinkedinUrl()
                )

                .researchPapersUrl(
                        faculty.getResearchPapersUrl()
                )

                // =========================
                // RESEARCH
                // =========================

                .researchInterests(
                        faculty.getResearchInterests()
                )

                // =========================
                // ADDRESS
                // =========================

                .currentAddress(
                        faculty.getCurrentAddress()
                )

                .permanentAddress(
                        faculty.getPermanentAddress()
                )

                // =========================
                // EMERGENCY CONTACT
                // =========================

                .emergencyContactName(
                        faculty.getEmergencyContactName()
                )

                .emergencyContactPhone(
                        faculty.getEmergencyContactPhone()
                )

                .build();
    }
}
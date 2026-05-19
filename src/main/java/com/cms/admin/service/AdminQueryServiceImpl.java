package com.cms.admin.service;

import com.cms.admin.dto.AdminDetailResponse;
import com.cms.admin.entity.Admin;
import com.cms.admin.repository.AdminRepository;
import com.cms.common.dto.DepartmentMiniResponse;
import com.cms.common.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cms.admin.dto.AdminSummaryResponse;
import com.cms.common.enums.Role;
import com.cms.common.exception.ForbiddenException;
import com.cms.common.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQueryServiceImpl implements AdminQueryService {

    private final AdminRepository adminRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminDetailResponse getMyProfile() {

        Admin admin = adminRepository.findById(
                        SecurityUtil.getCurrentUserId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Admin not found"));

        return mapToAdminDetailResponse(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminSummaryResponse> getAdmins(
            int page,
            int size
    ) {

        Role role = SecurityUtil.getCurrentUserRole();

        UUID currentUserId = SecurityUtil.getCurrentUserId();

        Pageable pageable = PageRequest.of(page, size);

        Page<Admin> adminPage;

        switch (role) {

            case SUPER_ADMIN -> {

                adminPage = adminRepository.findAllBy(
                        pageable
                );
            }

            case ADMIN -> {

                adminPage = adminRepository.findByUserId(
                        currentUserId,
                        pageable
                );
            }

            default -> throw new ForbiddenException(
                    "You are not allowed to access admins"
            );
        }

        return adminPage.map(admin ->

                AdminSummaryResponse.builder()

                        .userId(admin.getUser().getId())

                        .username(
                                admin.getUser().getUsername()
                        )

                        .institutionalEmail(
                                admin.getUser().getEmail()
                        )

                        .employeeId(
                                admin.getEmployeeId()
                        )

                        .fullName(
                                admin.getFullName()
                        )

                        .phoneNumber(
                                admin.getPhoneNumber()
                        )

                        .departmentName(
                                admin.getDepartment().getName()
                        )

                        .designation(
                                admin.getDesignation().name()
                        )

                        .status(
                                admin.getUser().getStatus()
                        )

                        .build()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public AdminDetailResponse getAdminById(
            UUID adminId
    ) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Admin not found"
                        ));

        validateAdminAccess(admin);

        return mapToAdminDetailResponse(admin);
    }

    private void validateAdminAccess(
            Admin admin
    ) {

        Role role = SecurityUtil.getCurrentUserRole();

        UUID currentUserId = SecurityUtil.getCurrentUserId();

        switch (role) {

            case SUPER_ADMIN -> {

                return;
            }

            case ADMIN -> {

                if (!admin.getUserId()
                        .equals(currentUserId)) {

                    throw new ForbiddenException(
                            "You cannot access this admin"
                    );
                }
            }

            default -> throw new ForbiddenException(
                    "Access denied"
            );
        }
    }


    private AdminDetailResponse
    mapToAdminDetailResponse(
            Admin admin
    ) {

        return AdminDetailResponse.builder()

                // =========================
                // AUTH / IDENTITY
                // =========================

                .userId(admin.getUser().getId())

                .username(admin.getUser().getUsername())

                .institutionalEmail(
                        admin.getUser().getEmail()
                )

                .status(
                        admin.getUser().getStatus()
                )

                // =========================
                // PROFESSIONAL
                // =========================

                .employeeId(admin.getEmployeeId())

                .designation(admin.getDesignation())

                .department(
                        DepartmentMiniResponse.builder()

                                .id(admin.getDepartment().getId())

                                .name(admin.getDepartment().getName())

                                .code(admin.getDepartment().getCode())

                                .build()
                )

                .joiningDate(admin.getJoiningDate())

                // =========================
                // BASIC PROFILE
                // =========================

                .fullName(admin.getFullName())

                .phoneNumber(admin.getPhoneNumber())

                .personalEmail(admin.getPersonalEmail())

                .dateOfBirth(admin.getDateOfBirth())

                .gender(admin.getGender())

                .bloodGroup(admin.getBloodGroup())

                // =========================
                // PROFILE LINKS
                // =========================

                .profileImageUrl(admin.getProfileImageUrl())

                .linkedinUrl(admin.getLinkedinUrl())

                .researchPapersUrl(
                        admin.getResearchPapersUrl()
                )

                // =========================
                // ADDRESS
                // =========================

                .currentAddress(admin.getCurrentAddress())

                .permanentAddress(
                        admin.getPermanentAddress()
                )

                // =========================
                // EMERGENCY CONTACT
                // =========================

                .emergencyContactName(
                        admin.getEmergencyContactName()
                )

                .emergencyContactPhone(
                        admin.getEmergencyContactPhone()
                )

                .build();
    }
}
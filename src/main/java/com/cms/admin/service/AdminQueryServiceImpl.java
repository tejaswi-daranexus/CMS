package com.cms.admin.service;

import com.cms.admin.dto.AdminDetailResponse;
import com.cms.admin.entity.Admin;
import com.cms.admin.repository.AdminRepository;
import com.cms.common.dto.DepartmentMiniResponse;
import com.cms.common.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return AdminDetailResponse.builder()

                // =========================
                // AUTH / IDENTITY
                // =========================

                .userId(admin.getUser().getId())
                .username(admin.getUser().getUsername())
                .institutionalEmail(admin.getUser().getEmail())
                .status(admin.getUser().getStatus())

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
                .researchPapersUrl(admin.getResearchPapersUrl())

                // =========================
                // ADDRESS
                // =========================

                .currentAddress(admin.getCurrentAddress())
                .permanentAddress(admin.getPermanentAddress())

                // =========================
                // EMERGENCY CONTACT
                // =========================

                .emergencyContactName(admin.getEmergencyContactName())
                .emergencyContactPhone(admin.getEmergencyContactPhone())

                .build();
    }
}
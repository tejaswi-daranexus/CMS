package com.cms.admin.service;

import com.cms.admin.dto.AdminDetailResponse;
import com.cms.admin.dto.AdminSummaryResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface AdminQueryService {

    AdminDetailResponse getMyProfile();

    Page<AdminSummaryResponse> getAdmins(
            int page,
            int size
    );

    AdminDetailResponse getAdminById(
            UUID adminId
    );
}
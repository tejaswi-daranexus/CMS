package com.cms.auth.controller;

import com.cms.admin.service.AdminQueryService;
import com.cms.common.enums.Role;
import com.cms.common.security.SecurityUtil;
import com.cms.faculty.service.FacultyQueryService;
import com.cms.student.service.StudentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/api/v1")
@RequiredArgsConstructor
public class MeController {

    private final StudentQueryService studentQueryService;

    private final FacultyQueryService facultyQueryService;

    private final AdminQueryService adminQueryService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {

        Role role = SecurityUtil.getCurrentUserRole();

        return switch (role) {

            case STUDENT ->
                    ResponseEntity.ok(
                            studentQueryService.getMyProfile()
                    );

            case FACULTY ->
                    ResponseEntity.ok(
                            facultyQueryService.getMyProfile()
                    );

            case ADMIN, SUPER_ADMIN ->
                    ResponseEntity.ok(
                            adminQueryService.getMyProfile()
                    );
        };
    }
}
package com.cms.student.dto;

public record StudentCreateRequest(
        String email,
        String password,
        String fullName,
        String phoneE164,
        String parentPhone
) {
}
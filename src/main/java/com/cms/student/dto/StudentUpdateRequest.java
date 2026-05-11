package com.cms.student.dto;

public record StudentUpdateRequest(
        String fullName,
        String phoneE164,
        String parentPhone
) {
}
package com.cms.student.dto;

import java.util.UUID;

public record StudentResponse(
        UUID userId,
        String fullName,
        String email,
        String phoneE164
) {
}
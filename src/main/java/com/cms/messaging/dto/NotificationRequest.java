package com.cms.messaging.dto;

import java.util.UUID;

public record NotificationRequest(
        UUID userId,
        String title,
        String message
) {
}
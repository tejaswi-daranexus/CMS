package com.cms.messaging.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        Long notificationId,
        UUID userId,
        String title,
        String message,
        boolean read,
        LocalDateTime createdAt
) {
}
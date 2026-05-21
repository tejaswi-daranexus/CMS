package com.cms.messaging.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponse(
        Long messageId,
        UUID senderId,
        UUID receiverId,
        String content,
        boolean readStatus,
        boolean critical,
        LocalDateTime createdAt
) {
}
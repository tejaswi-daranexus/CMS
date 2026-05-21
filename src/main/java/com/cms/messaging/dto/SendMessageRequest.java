package com.cms.messaging.dto;

import java.util.UUID;

public record SendMessageRequest(
        UUID senderId,
        UUID receiverId,
        String content,
        boolean critical
) {
}

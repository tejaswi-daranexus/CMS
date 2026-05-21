package com.cms.messaging.dto;

import java.util.UUID;

public record BroadcastRequest(
        UUID batchId,
        String title,
        String message
) {
}
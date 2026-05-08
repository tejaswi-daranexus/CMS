package com.cms.auth.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BulkUserRequest {
    private List<UUID> userIds;
}
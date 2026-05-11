package com.cms.batch.dto;

import com.cms.batch.entity.Batch;

import java.time.LocalDate;
import java.util.UUID;

public record BatchResponse(
        UUID id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        Batch.Status status
) {
}
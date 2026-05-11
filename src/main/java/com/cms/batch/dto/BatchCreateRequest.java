package com.cms.batch.dto;

import com.cms.batch.entity.Batch;

import java.time.LocalDate;

public record BatchCreateRequest(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        Batch.Status status
) {
}
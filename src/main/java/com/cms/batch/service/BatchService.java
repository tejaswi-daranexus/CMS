package com.cms.batch.service;

import com.cms.batch.dto.BatchCreateRequest;
import com.cms.batch.dto.BatchResponse;

import java.util.List;
import java.util.UUID;

public interface BatchService {

    BatchResponse createBatch(BatchCreateRequest request);

    List<BatchResponse> getAllBatches();

    BatchResponse updateBatch(UUID batchId, BatchCreateRequest request);

    void deleteBatch(UUID batchId);

    void assignStudent(UUID batchId, UUID studentId);

    void removeStudent(UUID studentId);
}
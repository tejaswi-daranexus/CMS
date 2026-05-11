package com.cms.batch.controller;

import com.cms.batch.dto.BatchCreateRequest;
import com.cms.batch.dto.BatchResponse;
import com.cms.batch.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    public ResponseEntity<BatchResponse> createBatch(
            @RequestBody BatchCreateRequest request
    ) {
        return ResponseEntity.ok(batchService.createBatch(request));
    }

    @GetMapping
    public ResponseEntity<List<BatchResponse>> getAllBatches() {
        return ResponseEntity.ok(batchService.getAllBatches());
    }

    @PutMapping("/{batchId}")
    public ResponseEntity<BatchResponse> updateBatch(
            @PathVariable UUID batchId,
            @RequestBody BatchCreateRequest request
    ) {
        return ResponseEntity.ok(
                batchService.updateBatch(batchId, request)
        );
    }

    @DeleteMapping("/{batchId}")
    public ResponseEntity<String> deleteBatch(
            @PathVariable UUID batchId
    ) {
        batchService.deleteBatch(batchId);
        return ResponseEntity.ok("Batch deleted successfully");
    }

    @PostMapping("/{batchId}/students/{studentId}")
    public ResponseEntity<String> assignStudent(
            @PathVariable UUID batchId,
            @PathVariable UUID studentId
    ) {
        batchService.assignStudent(batchId, studentId);
        return ResponseEntity.ok("Student assigned successfully");
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<String> removeStudent(
            @PathVariable UUID studentId
    ) {
        batchService.removeStudent(studentId);
        return ResponseEntity.ok("Student removed from batch");
    }
}
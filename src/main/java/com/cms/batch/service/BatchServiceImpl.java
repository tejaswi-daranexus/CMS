package com.cms.batch.service;

import com.cms.batch.dto.BatchCreateRequest;
import com.cms.batch.dto.BatchResponse;
import com.cms.batch.entity.Batch;
import com.cms.batch.repository.BatchRepository;
import com.cms.student.entity.Student;
import com.cms.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final StudentRepository studentRepository;

    @Override
    public BatchResponse createBatch(BatchCreateRequest request) {

        Batch batch = new Batch();
        batch.setName(request.name());
        batch.setStartDate(request.startDate());
        batch.setEndDate(request.endDate());
        batch.setStatus(request.status());

        batchRepository.save(batch);

        return mapToResponse(batch);
    }

    @Override
    public List<BatchResponse> getAllBatches() {
        return batchRepository.findByDeletedAtIsNull()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BatchResponse updateBatch(UUID batchId, BatchCreateRequest request) {

        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        batch.setName(request.name());
        batch.setStartDate(request.startDate());
        batch.setEndDate(request.endDate());
        batch.setStatus(request.status());

        batchRepository.save(batch);

        return mapToResponse(batch);
    }

    @Override
    public void deleteBatch(UUID batchId) {

        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        batch.setDeletedAt(LocalDateTime.now());

        batchRepository.save(batch);
    }

    @Override
    public void assignStudent(UUID batchId, UUID studentId) {

        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setBatch(batch);

        studentRepository.save(student);
    }

    @Override
    public void removeStudent(UUID studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setBatch(null);

        studentRepository.save(student);
    }

    private BatchResponse mapToResponse(Batch batch) {
        return new BatchResponse(
                batch.getId(),
                batch.getName(),
                batch.getStartDate(),
                batch.getEndDate(),
                batch.getStatus()
        );
    }
}
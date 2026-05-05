package com.cms.batch.entity;

import com.cms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "batches")
@Getter
@Setter
public class Batch extends BaseEntity {

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE,
        COMPLETED,
        ARCHIVED
    }
}
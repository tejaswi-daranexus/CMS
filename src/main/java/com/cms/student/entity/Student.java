package com.cms.student.entity;

import com.cms.auth.entity.User;
import com.cms.batch.entity.Batch;
import com.cms.common.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student extends BaseAuditEntity {

    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String fullName;

    private String phoneE164;

    private String parentPhone;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;
}
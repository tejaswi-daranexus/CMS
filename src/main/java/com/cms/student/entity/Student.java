package com.cms.student.entity;

import com.cms.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {

    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String fullName;

    private String phoneE164;

    private String parentPhone;

    private UUID batchId;
}
package com.cms.section.entity;

import com.cms.branch.entity.Branch;
import com.cms.common.entity.BaseEntity;
import com.cms.session.entity.Session;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "sections")
@Getter
@Setter
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private Integer capacity;
}
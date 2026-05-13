package com.cms.attendance.entity;

import com.cms.common.entity.BaseEntity;
import com.cms.sessionsubject.entity.SessionSubject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "attendance_sessions")
@Getter
@Setter
public class AttendanceSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_subject_id", nullable = false)
    private SessionSubject sessionSubject;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "topic_covered", length = 1000)
    private String topicCovered;
}
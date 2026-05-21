package com.cms.messaging.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID senderId;

    private UUID receiverId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean readStatus;

    private boolean critical;

    private LocalDateTime createdAt;
}
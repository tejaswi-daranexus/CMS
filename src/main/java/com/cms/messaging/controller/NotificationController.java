package com.cms.messaging.controller;

import com.cms.messaging.dto.*;
import com.cms.messaging.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public NotificationResponse createNotification(
            @RequestBody NotificationRequest request) {
        return notificationService.createNotification(request);
    }

    @GetMapping("/{userId}")
    public List<NotificationResponse> getNotifications(
            @PathVariable UUID userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PutMapping("/{notificationId}/read")
    public NotificationResponse markAsRead(
            @PathVariable Long notificationId) {
        return notificationService.markAsRead(notificationId);
    }
}
package com.cms.messaging.service;

import com.cms.messaging.dto.*;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    List<NotificationResponse> getUserNotifications(UUID userId);

    NotificationResponse markAsRead(Long notificationId);
}
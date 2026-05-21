package com.cms.messaging.service;

import com.cms.messaging.dto.*;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponse sendMessage(SendMessageRequest request);

    List<MessageResponse> getInbox(UUID receiverId);

    List<MessageResponse> getSentMessages(UUID senderId);

    String broadcastMessage(BroadcastRequest request);
}
package com.cms.messaging.service;

import com.cms.messaging.dto.*;
import com.cms.messaging.entity.Message;
import com.cms.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public MessageResponse sendMessage(SendMessageRequest request) {

        Message message = Message.builder()
                .senderId(request.senderId())
                .receiverId(request.receiverId())
                .content(request.content())
                .critical(request.critical())
                .readStatus(false)
                .createdAt(LocalDateTime.now())
                .build();

        Message saved = messageRepository.save(message);

        return mapToResponse(saved);
    }

    @Override
    public List<MessageResponse> getInbox(UUID receiverId) {
        return messageRepository.findByReceiverId(receiverId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MessageResponse> getSentMessages(UUID senderId) {
        return messageRepository.findBySenderId(senderId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public String broadcastMessage(BroadcastRequest request) {
        return "Broadcast sent successfully for batch: " + request.batchId();
    }

    private MessageResponse mapToResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent(),
                message.isReadStatus(),
                message.isCritical(),
                message.getCreatedAt()
        );
    }
}
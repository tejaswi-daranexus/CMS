package com.cms.messaging.controller;

import com.cms.messaging.dto.*;
import com.cms.messaging.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public MessageResponse sendMessage(@RequestBody SendMessageRequest request) {
        return messageService.sendMessage(request);
    }

    @GetMapping("/inbox/{receiverId}")
    public List<MessageResponse> getInbox(@PathVariable UUID receiverId) {
        return messageService.getInbox(receiverId);
    }

    @GetMapping("/sent/{senderId}")
    public List<MessageResponse> getSentMessages(@PathVariable UUID senderId) {
        return messageService.getSentMessages(senderId);
    }

    @PostMapping("/broadcast")
    public String broadcast(@RequestBody BroadcastRequest request) {
        return messageService.broadcastMessage(request);
    }
}
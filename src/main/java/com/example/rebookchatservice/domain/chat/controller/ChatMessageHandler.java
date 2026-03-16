package com.example.rebookchatservice.domain.chat.controller;

import com.example.rebookchatservice.domain.chat.dto.request.ChatMessageRequest;
import com.example.rebookchatservice.domain.chat.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageHandler {

    private final ChatService chatService;

    @MessageMapping("/api/chats/enter")
    public void enterChat(@Valid ChatMessageRequest chatMessage) {
        chatService.enterEvent(chatMessage);
    }

    @MessageMapping("/api/chats/message")
    public void receiveMessage(@Valid ChatMessageRequest request) throws JsonProcessingException {
        chatService.receiveMessage(request);
    }

    @MessageMapping("/api/chats/chat/exit")
    public void exit(ChatMessageRequest request) {
        chatService.leaveMessage(request);
    }
}

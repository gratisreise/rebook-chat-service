package com.example.rebookchatservice.domain.chat.dto;

import com.example.rebookchatservice.domain.chat.entity.ChatMessage;
import java.time.LocalDateTime;

public record ChatMessageResponse(
    String type,
    Long roomId,
    String senderId,
    String message,
    LocalDateTime sendAt
) {
    public ChatMessageResponse(ChatMessage chatMessage) {
        this(
            chatMessage.getType(),
            chatMessage.getRoomId(),
            chatMessage.getSenderId(),
            chatMessage.getMessage(),
            chatMessage.getSendAt()
        );
    }
}

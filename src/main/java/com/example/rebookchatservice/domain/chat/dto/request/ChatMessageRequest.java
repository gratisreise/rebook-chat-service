package com.example.rebookchatservice.domain.chat.dto.request;

import com.example.rebookchatservice.domain.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatMessageRequest(
    String type,
    Long roomId,
    String sender,
    String senderId,
    String receiverId,
    String message,
    LocalDateTime sendAt
) {
    public static ChatMessageRequest of(
        String type,
        Long roomId,
        String sender,
        String senderId,
        String receiverId,
        String message,
        LocalDateTime sendAt
    ) {
        return ChatMessageRequest.builder()
            .type(type)
            .roomId(roomId)
            .sender(sender)
            .senderId(senderId)
            .receiverId(receiverId)
            .message(message)
            .sendAt(sendAt)
            .build();
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
            .type(this.type)
            .roomId(this.roomId)
            .sender(this.sender)
            .senderId(this.senderId)
            .receiverId(this.receiverId)
            .message(this.message)
            .sendAt(this.sendAt)
            .build();
    }

    public ChatMessageRequest withMessage(String message) {
        return ChatMessageRequest.builder()
            .type(this.type)
            .roomId(this.roomId)
            .sender(this.sender)
            .senderId(this.senderId)
            .receiverId(this.receiverId)
            .message(message)
            .sendAt(this.sendAt)
            .build();
    }

    public ChatMessageRequest withType(String type) {
        return ChatMessageRequest.builder()
            .type(type)
            .roomId(this.roomId)
            .sender(this.sender)
            .senderId(this.senderId)
            .receiverId(this.receiverId)
            .message(this.message)
            .sendAt(this.sendAt)
            .build();
    }
}

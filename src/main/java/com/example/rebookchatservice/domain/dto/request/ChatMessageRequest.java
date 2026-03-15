package com.example.rebookchatservice.domain.dto.request;

import java.time.LocalDateTime;

public record ChatMessageRequest(
    String type,
    Long roomId,
    String sender,
    String senderId,
    String receiverId,
    String message,
    LocalDateTime sendAt
) {
    public ChatMessageRequest withMessage(String message) {
        return new ChatMessageRequest(this.type, this.roomId, this.sender, this.senderId, this.receiverId, message, this.sendAt);
    }

    public ChatMessageRequest withType(String type) {
        return new ChatMessageRequest(type, this.roomId, this.sender, this.senderId, this.receiverId, this.message, this.sendAt);
    }
}

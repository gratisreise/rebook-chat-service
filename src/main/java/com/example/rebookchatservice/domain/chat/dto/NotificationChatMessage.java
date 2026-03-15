package com.example.rebookchatservice.domain.chat.dto;

import java.io.Serializable;

public record NotificationChatMessage(
    String message,
    String type,
    String userId,
    String roomId
) implements Serializable {

    public NotificationChatMessage(ChatMessageRequest request, String message) {
        this(
            message,
            "CHAT",
            request.receiverId(),
            request.roomId().toString()
        );
    }
}

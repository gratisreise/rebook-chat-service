package com.example.rebookchatservice.domain.chat.dto.response;

import com.example.rebookchatservice.domain.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatMessageResponse(
    String type,
    Long roomId,
    String senderId,
    String message,
    LocalDateTime sendAt
) {
    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
            .type(chatMessage.getType())
            .roomId(chatMessage.getRoomId())
            .senderId(chatMessage.getSenderId())
            .message(chatMessage.getMessage())
            .sendAt(chatMessage.getSendAt())
            .build();
    }

    public static ChatMessageResponse of(
        String type,
        Long roomId,
        String senderId,
        String message,
        LocalDateTime sendAt
    ) {
        return ChatMessageResponse.builder()
            .type(type)
            .roomId(roomId)
            .senderId(senderId)
            .message(message)
            .sendAt(sendAt)
            .build();
    }
}

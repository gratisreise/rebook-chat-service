package com.example.rebookchatservice.domain.chat.dto.response;

import com.example.rebookchatservice.domain.chat.entity.ChatRoom;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatRoomResponse(
    Long id,
    String myId,
    String yourId,
    LocalDateTime createdAt,
    Long unreadCount
) {
    public static ChatRoomResponse from(ChatRoom chatRoom, String myId) {
        return ChatRoomResponse.builder()
            .id(chatRoom.getId())
            .myId(chatRoom.getUser1Id())
            .yourId(myId.equals(chatRoom.getUser1Id()) ? chatRoom.getUser2Id() : chatRoom.getUser1Id())
            .createdAt(chatRoom.getCreatedAt())
            .unreadCount(0L)
            .build();
    }

    public static ChatRoomResponse of(
        Long id,
        String myId,
        String yourId,
        LocalDateTime createdAt,
        Long unreadCount
    ) {
        return ChatRoomResponse.builder()
            .id(id)
            .myId(myId)
            .yourId(yourId)
            .createdAt(createdAt)
            .unreadCount(unreadCount)
            .build();
    }

    public ChatRoomResponse withUnreadCount(Long unreadCount) {
        return ChatRoomResponse.builder()
            .id(this.id)
            .myId(this.myId)
            .yourId(this.yourId)
            .createdAt(this.createdAt)
            .unreadCount(unreadCount)
            .build();
    }
}

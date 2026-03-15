package com.example.rebookchatservice.domain.dto.response;

import com.example.rebookchatservice.domain.entity.ChatRoom;
import java.time.LocalDateTime;

public record ChatRoomResponse(
    Long id,
    String myId,
    String yourId,
    LocalDateTime createdAt,
    Long unreadCount
) {
    public ChatRoomResponse(ChatRoom chatRoom, String myId) {
        this(
            chatRoom.getId(),
            chatRoom.getUser1Id(),
            myId.equals(chatRoom.getUser1Id()) ? chatRoom.getUser2Id() : chatRoom.getUser1Id(),
            chatRoom.getCreatedAt(),
            0L
        );
    }

    public ChatRoomResponse withUnreadCount(Long unreadCount) {
        return new ChatRoomResponse(this.id, this.myId, this.yourId, this.createdAt, unreadCount);
    }
}

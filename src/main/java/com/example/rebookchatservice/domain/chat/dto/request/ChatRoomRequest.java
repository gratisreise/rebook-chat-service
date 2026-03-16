package com.example.rebookchatservice.domain.chat.dto.request;

import com.example.rebookchatservice.domain.chat.entity.ChatRoom;
import lombok.Builder;

@Builder
public record ChatRoomRequest(
    String yourId,
    Long tradingId
) {
    public static ChatRoomRequest of(String yourId, Long tradingId) {
        return ChatRoomRequest.builder()
            .yourId(yourId)
            .tradingId(tradingId)
            .build();
    }

    public ChatRoom toEntity(String myId) {
        return ChatRoom.builder()
            .user1Id(myId)
            .user2Id(this.yourId)
            .tradingId(this.tradingId)
            .build();
    }
}

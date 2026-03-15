package com.example.rebookchatservice.domain.chat.dto;

public record ChatRoomRequest(
    String yourId,
    Long tradingId
) {
}

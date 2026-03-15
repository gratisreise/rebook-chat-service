package com.example.rebookchatservice.domain.dto.request;

public record ChatRoomRequest(
    String yourId,
    Long tradingId
) {
}

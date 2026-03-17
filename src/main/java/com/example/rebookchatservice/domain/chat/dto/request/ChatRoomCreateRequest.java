package com.example.rebookchatservice.domain.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChatRoomCreateRequest(

    @NotBlank String user1Id,
    @NotBlank String user2Id,
    @NotNull Long tradingId
) {
}

package com.example.rebookchatservice.domain.chat.controller;

import com.example.rebookchatservice.domain.chat.dto.request.ChatRoomCreateRequest;
import com.example.rebookchatservice.domain.chat.dto.request.ChatRoomRequest;
import com.example.rebookchatservice.domain.chat.dto.response.ChatMessageResponse;
import com.example.rebookchatservice.domain.chat.dto.response.ChatRoomResponse;
import com.example.rebookchatservice.domain.chat.service.ChatService;
import com.rebook.common.auth.PassportProto.Passport;
import com.rebook.common.auth.PassportUser;
import com.rebook.common.core.response.PageResponse;
import com.rebook.common.core.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    // ========== REST API ==========

    @GetMapping("/test")
    public String test(@PassportUser Passport passport) {
        return passport.toString();
    }

    @PostMapping("/{yourId}")
    public ResponseEntity<SuccessResponse<Long>> createChatRoom(
        @RequestBody @Valid ChatRoomCreateRequest request) {
        return SuccessResponse.toCreated(chatService.createChatRoom(request));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<ChatRoomResponse>>> getMyChatRooms(
        @PassportUser String myId,
        @PageableDefault Pageable pageable) {
        return SuccessResponse.toOk(chatService.getMyChatRooms(myId, pageable));
    }

    @GetMapping("/messages/{roomId}")
    public ResponseEntity<SuccessResponse<PageResponse<ChatMessageResponse>>> getMessages(
        @PathVariable Long roomId,
        @PageableDefault Pageable pageable) {
        return SuccessResponse.toOk(chatService.getMessages(roomId, pageable));
    }
}

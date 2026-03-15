package com.example.rebookchatservice.domain.controller;

import com.example.rebookchatservice.common.PageResponse;
import com.example.rebookchatservice.common.ResponseService;
import com.example.rebookchatservice.common.SingleResult;
import com.example.rebookchatservice.domain.dto.request.ChatRoomRequest;
import com.example.rebookchatservice.domain.dto.response.ChatRoomResponse;
import com.example.rebookchatservice.global.passport.PassportUser;
import com.example.rebookchatservice.domain.service.ChatRoomService;
import com.rebook.passport.PassportProto.Passport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Tag(name = "채팅방API")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/test")
    public String test(@PassportUser Passport passport) {
        return passport.toString();
    }

    @PostMapping("/{yourId}")
    @Operation(summary = "채팅방생성")
    public SingleResult<Long> createChatRoom(
        @RequestHeader("X-User-Id") String myId,
        @RequestBody ChatRoomRequest request) {
        return ResponseService.getSingleResult(chatRoomService.createChatRoom(myId, request));
    }

    @GetMapping
    @Operation(summary = "내채팅방조회")
    public SingleResult<PageResponse<ChatRoomResponse>> getMyChatRooms(
        @RequestHeader("X-User-Id") String myId,
        @PageableDefault Pageable pageable) {
        return ResponseService.getSingleResult(chatRoomService.getMyChatRooms(myId, pageable));
    }
}

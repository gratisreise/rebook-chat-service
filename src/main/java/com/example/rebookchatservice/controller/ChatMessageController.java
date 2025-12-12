package com.example.rebookchatservice.controller;

import com.example.rebookchatservice.common.PageResponse;
import com.example.rebookchatservice.common.ResponseService;
import com.example.rebookchatservice.common.SingleResult;
import com.example.rebookchatservice.model.ChatMessageRequest;
import com.example.rebookchatservice.model.ChatMessageResponse;
import com.example.rebookchatservice.service.ChatMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    //채팅방 입장
    @MessageMapping("/api/chats/enter")
    public void enterChat(@Valid ChatMessageRequest chatMessage) {
        chatMessageService.enterEvent(chatMessage);
    }

    // 메시지 수신 및 저장, 브로드캐스트
    @MessageMapping("/api/chats/message")
    public void receiveMessage(@Valid ChatMessageRequest request) throws JsonProcessingException {
        chatMessageService.receiveMessage(request);
    }

    //채팅방 퇴장
    @MessageMapping("/api/chats/chat/exit")
    public void exit(ChatMessageRequest request) {
        chatMessageService.leaveMessage(request);
    }

    //채팅 메세지 조회
    @GetMapping("/message/{roomId}")
    @Operation(summary = "채팅메세지조회")
    public SingleResult<PageResponse<ChatMessageResponse>> getRecentMessage(
        @PathVariable Long roomId, @PageableDefault Pageable pageable) {
        return ResponseService.getSingleResult(
            chatMessageService.getRecentMessage(roomId, pageable));
    }
}

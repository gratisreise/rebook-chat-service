package com.example.rebookchatservice.domain.chat.service;

import com.rebook.common.core.response.PageResponse;
import com.example.rebookchatservice.domain.chat.dto.request.ChatMessageRequest;
import com.example.rebookchatservice.domain.chat.dto.request.ChatRoomRequest;
import com.example.rebookchatservice.domain.chat.dto.response.ChatMessageResponse;
import com.example.rebookchatservice.domain.chat.dto.response.ChatRoomResponse;
import com.example.rebookchatservice.domain.chat.service.reader.ChatMessageReader;
import com.example.rebookchatservice.domain.chat.service.reader.ChatRoomReader;
import com.example.rebookchatservice.domain.chat.service.writer.ChatMessageWriter;
import com.example.rebookchatservice.domain.chat.service.writer.ChatRoomWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomWriter chatRoomWriter;
    private final ChatMessageWriter chatMessageWriter;
    private final ChatRoomReader chatRoomReader;
    private final ChatMessageReader chatMessageReader;

    // ========== Chat Room ==========

    public Long createChatRoom(String myId, ChatRoomRequest request) {
        return chatRoomWriter.createChatRoom(myId, request);
    }

    public PageResponse<ChatRoomResponse> getMyChatRooms(String myId, Pageable pageable) {
        return chatRoomReader.getMyChatRoomsWithUnread(myId, pageable);
    }

    // ========== Chat Message ==========

    public PageResponse<ChatMessageResponse> getMessages(Long roomId, Pageable pageable) {
        return chatMessageReader.getRecentMessage(roomId, pageable);
    }

    // ========== WebSocket Events ==========

    public void enterEvent(ChatMessageRequest request) {
        chatMessageWriter.enterEvent(request);
    }

    public void receiveMessage(ChatMessageRequest request) throws JsonProcessingException {
        chatMessageWriter.receiveMessage(request);
    }

    public void leaveMessage(ChatMessageRequest request) {
        chatMessageWriter.leaveMessage(request);
    }
}

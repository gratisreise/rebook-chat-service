package com.example.rebookchatservice.domain.service;

import com.example.rebookchatservice.common.PageResponse;
import com.example.rebookchatservice.domain.dto.request.ChatMessageRequest;
import com.example.rebookchatservice.domain.dto.response.ChatMessageResponse;
import com.example.rebookchatservice.domain.entity.ChatMessage;
import com.example.rebookchatservice.domain.entity.OutboxMessage;
import com.example.rebookchatservice.domain.repository.ChatMessageRepository;
import com.example.rebookchatservice.domain.repository.OutboxMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatReadStatusService chatReadStatusService;
    private final ObjectMapper objectMapper;
    private final OutboxMessageRepository outboxMessageRepository;

    public void enterEvent(ChatMessageRequest request) {
        ChatMessageRequest enterRequest = request.withMessage(request.sender() + "님이 입장하셨습니다.").withType("ENTER");

        chatReadStatusService.patchLastRead(enterRequest.roomId(), enterRequest.senderId());

        String destination = "/topic/room/" + enterRequest.roomId();

        log.info("destination: {}", destination);
        messagingTemplate.convertAndSend(destination, enterRequest);
    }

    @Transactional
    public void receiveMessage(ChatMessageRequest request) throws JsonProcessingException {
        saveChatMessage(request);

        String prefix = "/topic/room";
        OutboxMessage out = new OutboxMessage();
        out.setRoutingKey(prefix + request.roomId());
        out.setPayload(objectMapper.writeValueAsString(request));
        outboxMessageRepository.save(out);
    }

    public void leaveMessage(ChatMessageRequest request) {
        ChatMessageRequest leaveRequest = request.withMessage(request.sender() + "님이 퇴장했습니다.").withType("LEAVE");

        chatReadStatusService.patchLastRead(leaveRequest.roomId(), leaveRequest.senderId());

        messagingTemplate.convertAndSend("/topic/room/" + leaveRequest.roomId(), leaveRequest);
    }

    public PageResponse<ChatMessageResponse> getRecentMessage(Long roomId, Pageable pageable) {
        Page<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId, pageable);
        Page<ChatMessageResponse> responses = messages.map(ChatMessageResponse::new);
        return new PageResponse<>(responses);
    }

    public long getUnreadCount(String myId, Long roomId) {
        LocalDateTime lastRead = chatReadStatusService.getLastRead(myId, roomId);
        log.info("last read: {}", lastRead);
        return chatMessageRepository.countByRoomIdAndSendAtAfter(roomId, lastRead);
    }

    private void saveChatMessage(ChatMessageRequest request) {
        ChatMessage chatMessage = new ChatMessage(request);
        chatMessageRepository.save(chatMessage);
    }
}

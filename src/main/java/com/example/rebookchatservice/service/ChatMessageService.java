package com.example.rebookchatservice.service;

import com.example.rebookchatservice.common.PageResponse;
import com.example.rebookchatservice.model.ChatMessageRequest;
import com.example.rebookchatservice.model.ChatMessageResponse;
import com.example.rebookchatservice.model.entity.ChatMessage;
import com.example.rebookchatservice.model.entity.OutboxMessage;
import com.example.rebookchatservice.repository.ChatMessageRepository;
import com.example.rebookchatservice.repository.OutboxMessageRepository;
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
        // 입장
        request.setMessage(request.getSender() + "님이 입장하셨습니다.");
        request.setType("ENTER");

        chatReadStatusService.patchLastRead(request.getRoomId(), request.getSenderId());

        // 해당 채팅방을 구독 중인 모든 클라이언트에게 입장 알림 브로드캐스트
        String destination = "/topic/room/" + request.getRoomId();

        log.info("destination: {}", destination);
        messagingTemplate.convertAndSend(destination, request);
    }

    @Transactional
    public void receiveMessage(ChatMessageRequest request) throws JsonProcessingException {
        //채팅내용저장 => 채팅내용이 이제 안전
        saveChatMessage(request);

        // outbox에 채팅전송내용 저장
        OutboxMessage out = new OutboxMessage();
        out.setRoutingKey(request.getRoomId().toString()); // routing key for AMQP
        out.setPayload(objectMapper.writeValueAsString(request));
        outboxMessageRepository.save(out);
    }

    public void leaveMessage(ChatMessageRequest request) {
        request.setMessage(request.getSender() + "님이 퇴장했습니다.");
        request.setType("LEAVE");

        chatReadStatusService.patchLastRead(request.getRoomId(), request.getSenderId());

        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId(), request);
    }

    //최근 메세지 조회
    public PageResponse<ChatMessageResponse> getRecentMessage(Long roomId, Pageable pageable) {
        Page<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId, pageable);
        Page<ChatMessageResponse> responses = messages.map(ChatMessageResponse::new);
        return new PageResponse<>(responses);
    }

    public long getUnreadCount(String myId, Long roomId) {
        //마지막 읽은 날짜 확인
        LocalDateTime lastRead = chatReadStatusService.getLastRead(myId, roomId);
        log.info("last read: {}", lastRead);
        //해당 날짜 이후 메세지 숫자 반환
        return chatMessageRepository.countByRoomIdAndSendAtAfter(roomId, lastRead);
    }

    private void saveChatMessage(ChatMessageRequest request){
        ChatMessage chatMessage = new ChatMessage(request);
        chatMessageRepository.save(chatMessage);
    }
}

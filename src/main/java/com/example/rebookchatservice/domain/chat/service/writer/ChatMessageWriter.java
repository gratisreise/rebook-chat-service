package com.example.rebookchatservice.domain.chat.service.writer;

import com.example.rebookchatservice.domain.outbox.OutBoxRepository;
import com.example.rebookchatservice.domain.chat.dto.request.ChatMessageRequest;
import com.example.rebookchatservice.domain.outbox.Outbox;
import com.example.rebookchatservice.domain.chat.repository.ChatMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageWriter {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatReadStatusWriter chatReadStatusWriter;
    private final ObjectMapper objectMapper;
    private final OutBoxRepository outBoxRepository;

    public void enterEvent(ChatMessageRequest request) {
        ChatMessageRequest enterRequest = request.withMessage(request.sender() + "님이 입장하셨습니다.").withType("ENTER");

        chatReadStatusWriter.patchLastRead(enterRequest.roomId(), enterRequest.senderId());

        String destination = "/topic/room/" + enterRequest.roomId();

        log.info("destination: {}", destination);
        messagingTemplate.convertAndSend(destination, enterRequest);
    }

    @Transactional
    public void receiveMessage(ChatMessageRequest request) throws JsonProcessingException {
        saveChatMessage(request);

        String prefix = "/topic/room";
        Outbox outbox = Outbox.builder()
            .routingKey(prefix + request.roomId())
            .payload(objectMapper.writeValueAsString(request))
            .build();
        outBoxRepository.save(outbox);
    }

    public void leaveMessage(ChatMessageRequest request) {
        ChatMessageRequest leaveRequest = request.withMessage(request.sender() + "님이 퇴장했습니다.").withType("LEAVE");

        chatReadStatusWriter.patchLastRead(leaveRequest.roomId(), leaveRequest.senderId());

        messagingTemplate.convertAndSend("/topic/room/" + leaveRequest.roomId(), leaveRequest);
    }

    private void saveChatMessage(ChatMessageRequest request) {
        chatMessageRepository.save(request.toEntity());
    }
}

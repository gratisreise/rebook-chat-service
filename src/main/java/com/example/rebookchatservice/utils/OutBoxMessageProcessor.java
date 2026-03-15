package com.example.rebookchatservice.utils;

import com.example.rebookchatservice.global.exception.CMissingDataException;
import com.example.rebookchatservice.domain.dto.request.ChatMessageRequest;
import com.example.rebookchatservice.domain.entity.Outbox;
import com.example.rebookchatservice.domain.entity.OutboxMessage;
import com.example.rebookchatservice.domain.dto.NotificationChatMessage;
import com.example.rebookchatservice.domain.repository.OutBoxRepository;
import com.example.rebookchatservice.domain.repository.OutboxMessageRepository;
import com.example.rebookchatservice.domain.service.ChatMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutBoxMessageProcessor {

    private final OutboxMessageRepository outboxMessageRepository;
    private final OutBoxRepository outBoxRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;


    @Scheduled(fixedDelay = 500)
    @Transactional
    public void pollAndPublish() {
        List<OutboxMessage> list = outboxMessageRepository.findTop200ByStatusOrderByCreatedAtAsc("PENDING");
        for (OutboxMessage o : list) {
            try {
                ChatMessageRequest request = objectMapper.readValue(o.getPayload(), ChatMessageRequest.class);
                rabbitTemplate.convertAndSend(o.getRoutingKey(), request);

                o.setProcessed();
                outboxMessageRepository.save(o);

                saveOutBox(request);
            } catch (Exception ex) {
                throw new CMissingDataException("메세지가 제대로 발행되지 못했습니다.");
            }
        }

    }

    private void saveOutBox(ChatMessageRequest request) throws JsonProcessingException {
        String message = "새로운 채팅이 도착했습니다.";
        NotificationChatMessage notificationChatMessage = new NotificationChatMessage(request, message);
        String payload = objectMapper.writeValueAsString(notificationChatMessage);
        Outbox outBox = new Outbox();
        outBox.setPayload(payload);
        outBoxRepository.save(outBox);
    }

}

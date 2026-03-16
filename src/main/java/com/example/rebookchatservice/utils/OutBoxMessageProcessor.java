package com.example.rebookchatservice.utils;

import com.example.rebookchatservice.common.enums.MessageStatus;
import com.example.rebookchatservice.common.exception.ChatException;
import com.example.rebookchatservice.domain.chat.dto.request.ChatMessageRequest;
import com.example.rebookchatservice.domain.outbox.Outbox;
import com.example.rebookchatservice.external.rabbitmq.NotificationChatMessage;
import com.example.rebookchatservice.domain.outbox.OutBoxRepository;
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

    private final OutBoxRepository outBoxRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 500)
    @Transactional
    public void pollAndPublish() {
        List<Outbox> list = outBoxRepository.findTop20ByStatusOrderByCreatedAtAsc(MessageStatus.PENDING);
        for (Outbox o : list) {
            try {
                ChatMessageRequest request = objectMapper.readValue(o.getPayload(), ChatMessageRequest.class);
                rabbitTemplate.convertAndSend(o.getRoutingKey(), request);

                o.setProcessed();
                outBoxRepository.save(o);
            } catch (Exception ex) {
                throw ChatException.messagePublishFailed();
            }
        }
    }
}

package com.example.rebookchatservice.external.rabbitmq;

import com.example.rebookchatservice.common.enums.MessageStatus;
import com.example.rebookchatservice.domain.outbox.Outbox;
import com.example.rebookchatservice.domain.outbox.OutBoxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {

    private final OutBoxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${notification.exchange}")
    private String exchange;

    @Value("${notification.routing-key}")
    private String routingKey;

    @Scheduled(fixedDelay = 2000)
    public void processOutbox() {

        List<Outbox> pendingEvents =
            outboxRepository.findTop20ByStatusOrderByCreatedAtAsc(MessageStatus.PENDING);

        for (Outbox event : pendingEvents) {
            try {
                NotificationChatMessage message = objectMapper.readValue(event.getPayload(), NotificationChatMessage.class);
                rabbitTemplate.convertAndSend(exchange, routingKey, message);

                event.setProcessed();
                outboxRepository.save(event);

            } catch (Exception e) {
                log.error("Failed to process outbox event", e);
            }
        }
    }
}

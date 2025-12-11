package com.example.rebookchatservice.utils;


import com.example.rebookchatservice.model.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    @RabbitListener(queues = "chat.message.queue")
    public void onMessage(ChatMessageRequest request) {
        String dest = "/topic/room/" + request.getRoomId();
        messagingTemplate.convertAndSend(dest, request);
    }
}

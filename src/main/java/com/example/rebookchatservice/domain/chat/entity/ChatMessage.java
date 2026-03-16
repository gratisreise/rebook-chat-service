package com.example.rebookchatservice.domain.chat.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatting")
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    private String id;
    private String type;
    private Long roomId;
    private String sender;
    private String senderId;
    private String receiverId;
    private String message;
    @CreatedDate
    private LocalDateTime sendAt;

    @Builder
    public ChatMessage(String type, Long roomId, String sender, String senderId, String receiverId, String message, LocalDateTime sendAt) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.sendAt = sendAt;
    }
}

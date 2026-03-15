package com.example.rebookchatservice.domain.chat.entity;

import com.example.rebookchatservice.domain.chat.dto.ChatMessageRequest;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatting")
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Id
    private String id;
    private String type;
    private Long roomId;
    private String senderId;
    private String message;
    @CreatedDate
    private LocalDateTime sendAt;

    public ChatMessage(ChatMessageRequest request) {
        this.type = request.type();
        this.roomId = request.roomId();
        this.senderId = request.senderId();
        this.message = request.message();
    }
}

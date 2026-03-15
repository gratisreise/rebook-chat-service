package com.example.rebookchatservice.domain.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatReadStatus {

    @EmbeddedId
    ChatReadStatusId id;

    @Column(nullable = false)
    private LocalDateTime lastRead;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom room;


    public ChatReadStatus(ChatReadStatusId statusId, ChatRoom room, LocalDateTime lastRead) {
        this.lastRead = lastRead;
        this.id = statusId;
        this.room = room;
    }
}

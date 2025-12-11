package com.example.rebookchatservice.model.entity;

import com.example.rebookchatservice.enums.MessageStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutboxMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String routingKey;     // ex: "room.123"

    @Lob
    private String payload;

    private LocalDateTime createdAt;

    // PENDING, SENT, FAILED 등 상태 관리
    @Enumerated(value= EnumType.STRING)
    private MessageStatus status;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (status == null) {
            this.status = MessageStatus.PENDING;
        }
    }

    public void setProcessed(){
        this.status = MessageStatus.PROCESSED;
    }
}

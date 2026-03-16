package com.example.rebookchatservice.domain.outbox;

import com.example.rebookchatservice.common.enums.MessageStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String routingKey;

    @Lob
    private String payload;

    @Enumerated(value = EnumType.STRING)
    private MessageStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    @Builder
    public Outbox(String routingKey, String payload, MessageStatus status, LocalDateTime createdAt, LocalDateTime processedAt) {
        this.routingKey = routingKey;
        this.payload = payload;
        this.status = status != null ? status : MessageStatus.PENDING;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.processedAt = processedAt;
    }

    public void setProcessed() {
        this.status = MessageStatus.PROCESSED;
        this.processedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (status == null) {
            this.status = MessageStatus.PENDING;
        }
    }
}

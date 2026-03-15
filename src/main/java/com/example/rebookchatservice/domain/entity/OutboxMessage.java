package com.example.rebookchatservice.domain.entity;

import com.example.rebookchatservice.common.enums.MessageStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String routingKey;

    @Lob
    private String payload;

    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
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

    public void setProcessed() {
        this.status = MessageStatus.PROCESSED;
    }
}

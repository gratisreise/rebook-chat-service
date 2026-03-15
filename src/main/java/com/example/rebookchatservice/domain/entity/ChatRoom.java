package com.example.rebookchatservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user1Id", "user2Id"}))
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String user1Id;

    @Column(nullable = false, length = 50)
    private String user2Id;

    @Column(nullable = false)
    private Long tradingId;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public ChatRoom(String user1Id, String user2Id, Long tradingId) {
        if (user1Id.compareTo(user2Id) < 0) {
            this.user1Id = user1Id;
            this.user2Id = user2Id;
        } else {
            this.user1Id = user2Id;
            this.user2Id = user1Id;
        }
        this.tradingId = tradingId;
    }
}

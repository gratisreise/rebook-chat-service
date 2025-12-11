package com.example.rebookchatservice.repository;

import com.example.rebookchatservice.model.entity.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {
    List<OutboxMessage> findTop200ByStatusOrderByCreatedAtAsc(String status);
}
package com.example.rebookchatservice.domain.repository;

import com.example.rebookchatservice.domain.entity.OutboxMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {
    List<OutboxMessage> findTop200ByStatusOrderByCreatedAtAsc(String status);
}

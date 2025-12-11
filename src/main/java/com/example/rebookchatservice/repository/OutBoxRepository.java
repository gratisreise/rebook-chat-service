package com.example.rebookchatservice.repository;

import com.example.rebookchatservice.enums.MessageStatus;
import com.example.rebookchatservice.model.entity.Outbox;
import com.example.rebookchatservice.model.entity.OutboxMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoxRepository extends JpaRepository<Outbox, Long> {


    List<Outbox> findTop20ByStatusOrderByCreatedAtAsc(MessageStatus messageStatus);
}




package com.example.rebookchatservice.domain.repository;

import com.example.rebookchatservice.common.enums.MessageStatus;
import com.example.rebookchatservice.domain.entity.Outbox;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findTop20ByStatusOrderByCreatedAtAsc(MessageStatus messageStatus);
}

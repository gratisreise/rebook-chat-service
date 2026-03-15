package com.example.rebookchatservice.domain.repository;

import com.example.rebookchatservice.domain.entity.ChatReadStatus;
import com.example.rebookchatservice.domain.entity.ChatReadStatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatReadStatusRepository extends JpaRepository<ChatReadStatus, ChatReadStatusId> {

}

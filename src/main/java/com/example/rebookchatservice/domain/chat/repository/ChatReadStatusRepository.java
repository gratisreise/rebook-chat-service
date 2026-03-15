package com.example.rebookchatservice.domain.chat.repository;

import com.example.rebookchatservice.domain.chat.entity.ChatReadStatus;
import com.example.rebookchatservice.domain.chat.entity.ChatReadStatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatReadStatusRepository extends JpaRepository<ChatReadStatus, ChatReadStatusId> {

}

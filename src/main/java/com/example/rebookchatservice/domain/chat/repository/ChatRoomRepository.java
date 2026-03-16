package com.example.rebookchatservice.domain.chat.repository;

import com.example.rebookchatservice.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Page<ChatRoom> findByUser1IdOrUser2Id(String myId, String myId1, Pageable pageable);

    boolean existsByUser1IdAndUser2Id(String user1Id, String user2Id);

    ChatRoom findByUser1IdAndUser2Id(String user1Id, String user2Id);
}

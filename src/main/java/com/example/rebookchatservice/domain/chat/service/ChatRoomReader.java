package com.example.rebookchatservice.domain.chat.service;

import com.example.rebookchatservice.global.exception.CMissingDataException;
import com.example.rebookchatservice.domain.chat.entity.ChatRoom;
import com.example.rebookchatservice.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatRoomReader {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow(CMissingDataException::new);
    }

    public Page<ChatRoom> getChatRooms(String myId, Pageable pageable) {
        return chatRoomRepository.findByUser1IdOrUser2Id(myId, myId, pageable);
    }

    public ChatRoom getChatRoom(String myId, String yourId) {
        if (myId.compareTo(yourId) < 0) {
            return chatRoomRepository.findByUser1IdAndUser2Id(myId, yourId);
        } else {
            return chatRoomRepository.findByUser1IdAndUser2Id(yourId, myId);
        }
    }
}

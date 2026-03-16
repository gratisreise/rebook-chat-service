package com.example.rebookchatservice.domain.chat.service.writer;

import com.example.rebookchatservice.common.exception.ChatException;
import com.example.rebookchatservice.domain.chat.dto.request.ChatRoomRequest;
import com.example.rebookchatservice.domain.chat.entity.ChatRoom;
import com.example.rebookchatservice.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomWriter {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatReadStatusWriter chatReadStatusWriter;

    @Transactional
    public Long createChatRoom(String myId, ChatRoomRequest request) {
        String yourId = request.yourId();
        if (yourId.equals(myId)) {
            throw ChatException.duplicatedChatRoom();
        }
        if (isRoomExists(myId, yourId)) {
            return getExistingRoomId(myId, yourId);
        }
        ChatRoom chatRoom = chatRoomRepository.save(request.toEntity(myId));
        chatReadStatusWriter.createChatReadStatus(myId, yourId, chatRoom.getId());
        return chatRoom.getId();
    }

    private boolean isRoomExists(String myId, String yourId) {
        return (myId.compareTo(yourId) < 0) ?
            chatRoomRepository.existsByUser1IdAndUser2Id(myId, yourId) :
            chatRoomRepository.existsByUser1IdAndUser2Id(yourId, myId);
    }

    private Long getExistingRoomId(String myId, String yourId) {
        ChatRoom existingRoom = (myId.compareTo(yourId) < 0) ?
            chatRoomRepository.findByUser1IdAndUser2Id(myId, yourId) :
            chatRoomRepository.findByUser1IdAndUser2Id(yourId, myId);
        return existingRoom.getId();
    }
}

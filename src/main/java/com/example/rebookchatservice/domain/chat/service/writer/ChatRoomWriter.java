package com.example.rebookchatservice.domain.chat.service.writer;

import com.example.rebookchatservice.common.exception.ChatException;
import com.example.rebookchatservice.domain.chat.dto.request.ChatRoomCreateRequest;
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
    public Long createChatRoom(ChatRoomCreateRequest request) {
        String user1Id = request.user1Id();
        String user2Id = request.user2Id();

        // 사전순 정렬
        if (user1Id.compareTo(user2Id) > 0) {
            String temp = user1Id;
            user1Id = user2Id;
            user2Id = temp;
        }

        //검증
        validateRoom(user1Id, user2Id);

        ChatRoom chatRoom = ChatRoom.builder()
            .user1Id(user1Id)
            .user2Id(user2Id)
            .tradingId(request.tradingId())
            .build();
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        chatReadStatusWriter.createChatReadStatus(user1Id, user2Id, savedRoom.getId());
        return savedRoom.getId();
    }

    private void validateRoom(String user1Id, String user2Id) {
        if(isRoomExists(user1Id, user2Id)){
            throw ChatException.duplicatedChatRoom();
        }

    }

    private boolean isRoomExists(String user1Id, String user2Id) {
        return chatRoomRepository.existsByUser1IdAndUser2Id(user1Id, user2Id);
    }

}

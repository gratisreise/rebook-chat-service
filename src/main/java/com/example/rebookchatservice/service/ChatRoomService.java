package com.example.rebookchatservice.service;

import com.example.rebookchatservice.common.PageResponse;
import com.example.rebookchatservice.model.ChatRoomResponse;
import com.example.rebookchatservice.model.entity.ChatRoom;
import com.example.rebookchatservice.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomReader chatRoomReader;
    private final ChatReadStatusService chatReadStatusService;
    private final ChatMessageService chatMessageService;

    @Transactional
    public Long createChatRoom(String myId, String yourId) {
        if (isRoomExists(myId, yourId)) {
            //코드변경필요 해당 객체의 아이디 조기 반환하는 걸로 변경
            return chatRoomReader.getChatRoom(myId, yourId).getId();
        }

        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(myId, yourId));
        chatReadStatusService.crateChatReadStatus(myId, yourId, chatRoom.getId());
        return chatRoom.getId();
    }

    private boolean isRoomExists(String myId, String yourId) {
        return (myId.compareTo(yourId) < 0) ?
            chatRoomRepository.existsByUser1IdAndUser2Id(myId, yourId) :
            chatRoomRepository.existsByUser1IdAndUser2Id(yourId, myId);
    }



    public PageResponse<ChatRoomResponse> getMyChatRooms(String myId, Pageable pageable) {
        Page<ChatRoom> rooms = chatRoomReader.getChatRooms(myId, pageable);
        Page<ChatRoomResponse> roomResponses = rooms.map(ChatRoomResponse::new);
        roomResponses.getContent().forEach(res -> {
            long unreadCount = chatMessageService.getUnreadCount(myId, res.getId());
            res.setUnreadCount(unreadCount);
        });
        return new PageResponse<>(roomResponses);
    }


}

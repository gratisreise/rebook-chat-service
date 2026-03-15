package com.example.rebookchatservice.domain.service;

import com.example.rebookchatservice.common.PageResponse;
import com.example.rebookchatservice.global.exception.CDuplicatedDataException;
import com.example.rebookchatservice.domain.dto.request.ChatRoomRequest;
import com.example.rebookchatservice.domain.dto.response.ChatRoomResponse;
import com.example.rebookchatservice.domain.entity.ChatRoom;
import com.example.rebookchatservice.domain.repository.ChatRoomRepository;
import java.util.List;
import java.util.stream.Collectors;
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
    public Long createChatRoom(String myId, ChatRoomRequest request) {
        String yourId = request.yourId();
        if (yourId.equals(myId)) {
            throw new CDuplicatedDataException("같은유저입니다.");
        }
        if (isRoomExists(myId, yourId)) {
            return chatRoomReader.getChatRoom(myId, yourId).getId();
        }
        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(myId, yourId, request.tradingId()));
        chatReadStatusService.createChatReadStatus(myId, yourId, chatRoom.getId());
        return chatRoom.getId();
    }

    private boolean isRoomExists(String myId, String yourId) {
        return (myId.compareTo(yourId) < 0) ?
            chatRoomRepository.existsByUser1IdAndUser2Id(myId, yourId) :
            chatRoomRepository.existsByUser1IdAndUser2Id(yourId, myId);
    }

    public PageResponse<ChatRoomResponse> getMyChatRooms(String myId, Pageable pageable) {
        Page<ChatRoom> rooms = chatRoomReader.getChatRooms(myId, pageable);

        List<ChatRoomResponse> contentWithUnread = rooms.getContent().stream()
            .map(room -> new ChatRoomResponse(room, myId))
            .map(res -> res.withUnreadCount(chatMessageService.getUnreadCount(myId, res.id())))
            .collect(Collectors.toList());

        Page<ChatRoomResponse> roomResponses = new org.springframework.data.domain.PageImpl<>(
            contentWithUnread,
            rooms.getPageable(),
            rooms.getTotalElements()
        );

        return new PageResponse<>(roomResponses);
    }
}

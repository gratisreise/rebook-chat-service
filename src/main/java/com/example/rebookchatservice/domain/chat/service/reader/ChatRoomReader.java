package com.example.rebookchatservice.domain.chat.service.reader;

import com.rebook.common.core.response.PageResponse;
import com.example.rebookchatservice.common.exception.ChatException;
import com.example.rebookchatservice.domain.chat.dto.response.ChatRoomResponse;
import com.example.rebookchatservice.domain.chat.entity.ChatRoom;
import com.example.rebookchatservice.domain.chat.repository.ChatRoomRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatRoomReader {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageReader chatMessageReader;
    private final ChatReadStatusReader chatReadStatusReader;

    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow(ChatException::chatRoomNotFound);
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

    public PageResponse<ChatRoomResponse> getMyChatRoomsWithUnread(String myId, Pageable pageable) {
        Page<ChatRoom> rooms = getChatRooms(myId, pageable);

        List<ChatRoomResponse> contentWithUnread = rooms.getContent().stream()
            .map(room -> ChatRoomResponse.from(room, myId))
            .map(res -> res.withUnreadCount(getUnreadCount(myId, res.id())))
            .collect(Collectors.toList());

        Page<ChatRoomResponse> roomResponses = new PageImpl<>(
            contentWithUnread,
            rooms.getPageable(),
            rooms.getTotalElements()
        );

        return PageResponse.from(roomResponses);
    }

    private long getUnreadCount(String myId, Long roomId) {
        return chatMessageReader.getUnreadCount(roomId, chatReadStatusReader.getLastRead(myId, roomId));
    }
}

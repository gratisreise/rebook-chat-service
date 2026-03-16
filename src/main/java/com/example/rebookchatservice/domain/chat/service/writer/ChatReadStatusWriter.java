package com.example.rebookchatservice.domain.chat.service.writer;

import com.example.rebookchatservice.common.exception.ChatException;
import com.example.rebookchatservice.domain.chat.entity.ChatReadStatus;
import com.example.rebookchatservice.domain.chat.entity.ChatRoom;
import com.example.rebookchatservice.domain.chat.entity.compositerkey.ChatReadStatusId;
import com.example.rebookchatservice.domain.chat.repository.ChatReadStatusRepository;
import com.example.rebookchatservice.domain.chat.repository.ChatRoomRepository;
import com.example.rebookchatservice.domain.chat.service.reader.ChatReadStatusReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatReadStatusWriter {

    private final ChatReadStatusReader chatReadStatusReader;
    private final ChatReadStatusRepository chatReadStatusRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void patchLastRead(Long roomId, String userId) {
        log.info("마지막읽은날짜 시작");
        ChatReadStatusId statusId = new ChatReadStatusId(roomId, userId);
        ChatReadStatus readStatus = chatReadStatusReader.findById(statusId);
        ZoneId seoulZone = ZoneId.of("Asia/Seoul");
        LocalDateTime lastRead = LocalDateTime.now(seoulZone);
        log.info("lastReadTime: {}", lastRead);
        readStatus.setLastRead(lastRead);
    }

    @Transactional
    public void createChatReadStatus(String myId, String yourId, Long roomId) {
        ZoneId seoulZone = ZoneId.of("Asia/Seoul");
        LocalDateTime lastRead = LocalDateTime.now(seoulZone);
        ChatReadStatus readStatus1 = generateChatReadStatus(myId, roomId, lastRead);
        ChatReadStatus readStatus2 = generateChatReadStatus(yourId, roomId, lastRead);
        chatReadStatusRepository.save(readStatus1);
        chatReadStatusRepository.save(readStatus2);
    }

    private ChatReadStatus generateChatReadStatus(String userId, Long roomId, LocalDateTime lastRead) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(ChatException::chatRoomNotFound);
        ChatReadStatusId statusId = new ChatReadStatusId(roomId, userId);
        return new ChatReadStatus(statusId, room, lastRead);
    }
}

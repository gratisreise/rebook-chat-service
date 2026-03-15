package com.example.rebookchatservice.domain.chat.service;

import com.example.rebookchatservice.global.exception.CMissingDataException;
import com.example.rebookchatservice.domain.chat.entity.ChatReadStatus;
import com.example.rebookchatservice.domain.chat.entity.ChatRoom;
import com.example.rebookchatservice.domain.chat.entity.ChatReadStatusId;
import com.example.rebookchatservice.domain.chat.repository.ChatReadStatusRepository;
import com.example.rebookchatservice.domain.chat.repository.ChatRoomRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatReadStatusService {

    private final ChatReadStatusReader chatReadStatusReader;
    private final ChatMessageReader chatMessageReader;
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

    public LocalDateTime getLastRead(String myId, Long roomId) {
        ChatReadStatusId statusId = new ChatReadStatusId(roomId, myId);
        ChatReadStatus readStatus = chatReadStatusReader.findById(statusId);
        return readStatus.getLastRead();
    }

    private ChatReadStatus generateChatReadStatus(String userId, Long roomId, LocalDateTime lastRead) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(CMissingDataException::new);
        ChatReadStatusId statusId = new ChatReadStatusId(roomId, userId);
        return new ChatReadStatus(statusId, room, lastRead);
    }
}

package com.example.rebookchatservice.domain.chat.service.reader;

import com.example.rebookchatservice.common.exception.ChatException;
import com.example.rebookchatservice.domain.chat.entity.ChatReadStatus;
import com.example.rebookchatservice.domain.chat.entity.compositerkey.ChatReadStatusId;
import com.example.rebookchatservice.domain.chat.repository.ChatReadStatusRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatReadStatusReader {

    private final ChatReadStatusRepository chatReadStatusRepository;

    public ChatReadStatus findById(ChatReadStatusId statusId) {
        return chatReadStatusRepository.findById(statusId).orElseThrow(ChatException::chatRoomNotFound);
    }

    public LocalDateTime getLastRead(String userId, Long roomId) {
        ChatReadStatusId statusId = new ChatReadStatusId(roomId, userId);
        ChatReadStatus readStatus = findById(statusId);
        return readStatus.getLastRead();
    }
}

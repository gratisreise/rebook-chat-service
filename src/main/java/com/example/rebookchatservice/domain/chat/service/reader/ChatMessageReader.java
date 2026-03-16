package com.example.rebookchatservice.domain.chat.service.reader;

import com.rebook.common.core.response.PageResponse;
import com.example.rebookchatservice.domain.chat.dto.response.ChatMessageResponse;
import com.example.rebookchatservice.domain.chat.entity.ChatMessage;
import com.example.rebookchatservice.domain.chat.repository.ChatMessageRepository;
import java.time.LocalDateTime;
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
public class ChatMessageReader {
    private final ChatMessageRepository chatMessageRepository;

    public PageResponse<ChatMessageResponse> getRecentMessage(Long roomId, Pageable pageable) {
        Page<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId, pageable);
        Page<ChatMessageResponse> responses = messages.map(ChatMessageResponse::from);
        return PageResponse.from(responses);
    }

    public long getUnreadCount(Long roomId, LocalDateTime lastReadAt) {
        return chatMessageRepository.countByRoomIdAndSendAtAfter(roomId, lastReadAt);
    }
}

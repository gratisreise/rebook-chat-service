package com.example.rebookchatservice.domain.chat.service;

import com.example.rebookchatservice.global.exception.CMissingDataException;
import com.example.rebookchatservice.domain.chat.entity.ChatReadStatus;
import com.example.rebookchatservice.domain.chat.entity.ChatReadStatusId;
import com.example.rebookchatservice.domain.chat.repository.ChatReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatReadStatusReader {

    private final ChatReadStatusRepository chatReadStatusRepository;

    public ChatReadStatus findById(ChatReadStatusId statusId) {
        return chatReadStatusRepository.findById(statusId).orElseThrow(CMissingDataException::new);
    }
}

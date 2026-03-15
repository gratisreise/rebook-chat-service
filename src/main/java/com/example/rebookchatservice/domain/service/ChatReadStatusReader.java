package com.example.rebookchatservice.domain.service;

import com.example.rebookchatservice.global.exception.CMissingDataException;
import com.example.rebookchatservice.domain.entity.ChatReadStatus;
import com.example.rebookchatservice.domain.entity.ChatReadStatusId;
import com.example.rebookchatservice.domain.repository.ChatReadStatusRepository;
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

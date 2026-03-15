package com.example.rebookchatservice.domain.chat.service;

import com.example.rebookchatservice.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatMessageReader {
    private final ChatMessageRepository chatMessageRepository;
}

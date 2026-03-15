package com.example.rebookchatservice.domain.controller;

import com.example.rebookchatservice.domain.service.ChatReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatReadStatusController {
    private final ChatReadStatusService chatReadStatusService;

}

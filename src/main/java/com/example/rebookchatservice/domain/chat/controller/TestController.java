package com.example.rebookchatservice.domain.chat.controller;

import com.example.rebookchatservice.common.CommonResult;
import com.example.rebookchatservice.common.ResponseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/ws-chat/test")
    public CommonResult test() {
        return ResponseService.getSuccessResult();
    }
}

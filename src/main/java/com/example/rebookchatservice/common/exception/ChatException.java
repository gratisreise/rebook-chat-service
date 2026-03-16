package com.example.rebookchatservice.common.exception;

import com.rebook.common.core.exception.BusinessException;
import com.rebook.common.core.exception.ErrorCode;

public class ChatException extends BusinessException {

    public ChatException() {
        super(ErrorCode.UNKNOWN_ERROR);
    }

    // 채팅방을 찾을 수 없음
    public static ChatException chatRoomNotFound() {
        return new ChatException();
    }

    // 중복된 채팅방 생성 시도
    public static ChatException duplicatedChatRoom() {
        return new ChatException();
    }

    // 메시지를 찾을 수 없음
    public static ChatException messageNotFound() {
        return new ChatException();
    }

    // 메시지 발행 실패
    public static ChatException messagePublishFailed() {
        return new ChatException();
    }

    // 권한 없음 (채팅방 참여자 아님)
    public static ChatException forbidden() {
        return new ChatException();
    }
}

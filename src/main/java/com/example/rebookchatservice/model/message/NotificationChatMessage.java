package com.example.rebookchatservice.model.message;

import com.example.rebookchatservice.model.ChatMessageRequest;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationChatMessage implements Serializable {
    private String message;
    private String type;
    private String userId;
    private String roomId;

    public NotificationChatMessage(ChatMessageRequest request, String message) {
        this.message = message;
        this.type = "CHAT";
        this.userId = request.getReceiverId();
        this.roomId = request.getRoomId().toString();
    }


}

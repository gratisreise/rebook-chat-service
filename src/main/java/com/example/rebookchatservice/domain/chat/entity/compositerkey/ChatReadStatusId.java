package com.example.rebookchatservice.domain.chat.entity.compositerkey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatReadStatusId implements Serializable {

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false, length = 50)
    private String userId;
}

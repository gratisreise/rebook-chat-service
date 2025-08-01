package com.example.rebookchatservice.model.entity.compositekey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatReadStatusId implements Serializable {

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false, length = 50)
    private String userId;
}

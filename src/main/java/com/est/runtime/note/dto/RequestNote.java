package com.est.runtime.note.dto;

import com.est.runtime.note.Note;
import com.est.runtime.signup.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestNote {
    private String receiver;
    private String message;

    public Note toEntity(String message, Member sender, Member receiver) {
        return Note.builder()
                .message(message)
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}

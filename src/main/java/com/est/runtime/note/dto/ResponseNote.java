package com.est.runtime.note.dto;

import com.est.runtime.note.Note;
import com.est.runtime.signup.dto.MemberForNoteDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseNote {
    private String message;
    private MemberForNoteDTO sender;
    private MemberForNoteDTO receiver;
    private LocalDateTime sentAt;

    public ResponseNote(Note note) {
        this.message = note.getMessage();
        this.sender = new MemberForNoteDTO(note.getSender());
        this.receiver = new MemberForNoteDTO(note.getReceiver());
        this.sentAt = note.getSentAt();
    }
}

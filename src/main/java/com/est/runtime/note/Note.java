package com.est.runtime.note;

import com.est.runtime.note.dto.ResponseNote;
import com.est.runtime.signup.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    @Builder
    public Note(String message, Member sender, Member receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public ResponseNote toDto() {
        return new ResponseNote(this);
    }
}

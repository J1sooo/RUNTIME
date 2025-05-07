package com.est.runtime.note;

import com.est.runtime.note.dto.RequestNote;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;

    public Note sendNote(Member sender, RequestNote request) {
        Member senderId = memberRepository.findById(sender.getId())
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));
        Member receiverId = memberRepository.findById(request.getReceiver())
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));

        Note note = request.toEntity(request.getMessage(), senderId, receiverId);

        return noteRepository.save(note);
    }

    public List<Note> receivedNote(Member receiver) {
        return noteRepository.findByReceiverId(receiver.getId());
    }

    public List<Note> sentNote(Member sender) {
        return noteRepository.findBySenderId(sender.getId());
    }

    public void deleteNote(Member sender, Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));
        if (sender.getId().equals(note.getSender().getId())) {
            noteRepository.deleteById(id);
        }
    }
}

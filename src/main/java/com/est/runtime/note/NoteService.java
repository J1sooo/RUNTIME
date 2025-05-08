package com.est.runtime.note;

import com.est.runtime.note.dto.RequestNote;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
        Member receiverId = memberRepository.findByUsername(request.getReceiver())
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

    public Boolean IsReadNote(Member receiver) {
        return noteRepository.existsByReceiverAndIsReadFalse(receiver);
    }

    @Transactional
    public void markAllAsRead(Member receiver) {
        List<Note> unreadNotes = noteRepository.findAllByReceiverAndIsReadFalse(receiver);
        for (Note note : unreadNotes) {
            note.readNote();
        }
    }
}

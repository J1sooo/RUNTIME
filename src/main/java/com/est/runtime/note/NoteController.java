package com.est.runtime.note;

import com.est.runtime.note.dto.RequestNote;
import com.est.runtime.note.dto.ResponseNote;
import com.est.runtime.signup.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/note")
@RequiredArgsConstructor
@RestController
public class NoteController {
    private final NoteService noteService;

    @PostMapping()
    public ResponseEntity<ResponseNote> send(@AuthenticationPrincipal Member sender, @RequestBody RequestNote request) {
        Note note = noteService.sendNote(sender, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(note.toDto());
    }

    @GetMapping("/received")
    public ResponseEntity<List<ResponseNote>> received(@AuthenticationPrincipal Member receiver) {
        List<ResponseNote> list = noteService.receivedNote(receiver)
                .stream()
                .map(Note::toDto)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/sent")
    public ResponseEntity<List<ResponseNote>> sent(@AuthenticationPrincipal Member sender) {
        List<ResponseNote> list = noteService.sentNote(sender)
                .stream()
                .map(Note::toDto)
                .toList();

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Member sender, @PathVariable Long id) {
        noteService.deleteNote(sender, id);
        return ResponseEntity.noContent().build();
    }
}

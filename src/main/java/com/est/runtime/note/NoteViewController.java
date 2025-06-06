package com.est.runtime.note;

import com.est.runtime.note.dto.RequestNote;
import com.est.runtime.signup.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class NoteViewController {
    private final NoteService noteService;

    @GetMapping("/notes")
    public String getNotes(@AuthenticationPrincipal Member member,
                           @RequestParam(defaultValue = "received") String type,
                           Model model) {
        noteService.markAllAsRead(member);

        List<Note> notes = type.equals("received") ? noteService.receivedNote(member) : noteService.sentNote(member);

        model.addAttribute("type", type);
        model.addAttribute("notes", notes);

        return "note";
    }

    @GetMapping("/note/new")
    public String postNote(Model model, @RequestParam(defaultValue = "") String id) {
        model.addAttribute("id", id);
        model.addAttribute("note", new RequestNote());
        return "newNote";
    }

    @GetMapping("/note/{id}")
    public String noteDetail(@PathVariable Long id, Model model, @RequestParam String type) {
        model.addAttribute("type", type);
        model.addAttribute("note", noteService.findById(id));
        return "noteDetail";
    }
}

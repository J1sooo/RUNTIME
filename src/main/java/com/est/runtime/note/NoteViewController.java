package com.est.runtime.note;

import com.est.runtime.note.dto.RequestNote;
import com.est.runtime.signup.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<Note> notes = type.equals("received") ? noteService.receivedNote(member) : noteService.sentNote(member);

        model.addAttribute("type", type);
        model.addAttribute("notes", notes);

        return "note";
    }
}

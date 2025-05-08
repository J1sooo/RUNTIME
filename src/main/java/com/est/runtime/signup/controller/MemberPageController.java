package com.est.runtime.signup.controller;

import com.est.runtime.signup.dto.MemberDTO;
import com.est.runtime.signup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberPageController {

    private final MemberService memberService;

    // GET: 로그인 창 띄우기
    @GetMapping("/login")
    public String displayLoginForm() {
        return "login";  // login.html
    }

    // GET: 인덱스
    @GetMapping("/index")
    public String displayMain() {
        return "index";  // index.html
    }

    // GET: 회원가입 폼 페이지 띄우기
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";  // save.html
    }

    // POST: 회원가입 폼 처리
    @PostMapping("/member/save")
    public String pageSave(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "login";  // 가입 후 로그인 페이지로 이동
    }
    // GET: 마이페이지 폼 띄우기
    @GetMapping("/mypage")
    public String displayMyPage(Model model, Principal principal) {
        String username = principal.getName();
        MemberDTO member = memberService.findByUsername(username);
        model.addAttribute("member", member);
        return "mypage"; // mypage.html
    }
}
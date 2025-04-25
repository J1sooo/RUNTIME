package com.est.runtime.signup.controller;

import com.est.runtime.signup.dto.MemberDTO;
import com.est.runtime.signup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberPageController {

    private final MemberService memberService;

    // GET: 회원가입 폼 페이지 띄우기
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";  // save.html
    }

    // POST: 폼 제출 처리
    @PostMapping("/member/save")
    public String pageSave(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "index";  // 가입 성공 후 이동할 페이지
    }
}
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
public class MemberController {

    private final MemberService memberService;

    // 회원가입 페이지 출력
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";  // save.html
    }

    // 회원가입 요청 처리
    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController save");
        System.out.println("memberDTO: " + memberDTO);
        memberService.save(memberDTO);
        return "index";  // 가입 후 리디렉션할 페이지
    }
}
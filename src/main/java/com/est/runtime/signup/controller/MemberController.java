package com.est.runtime.signup.controller;

import com.est.runtime.signup.dto.MemberDTO;
import com.est.runtime.signup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/save")
    public ResponseEntity<String> save(@RequestBody MemberDTO memberDTO) {
        System.out.println("MemberController save");
        System.out.println("memberDTO: " + memberDTO);
        memberService.save(memberDTO);
        return ResponseEntity.ok("회원가입 성공");
    }
}
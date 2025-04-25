package com.est.runtime.signup.controller;

import com.est.runtime.signup.dto.MemberDTO;
import com.est.runtime.signup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return ResponseEntity.ok("회원가입 성공");
    }
}
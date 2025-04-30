package com.est.runtime.signup.controller;

import com.est.runtime.signup.dto.MemberDeleteDTO;
import com.est.runtime.signup.dto.MemberInfoUpdateDTO;
import com.est.runtime.signup.dto.MemberUpdateResponse;
import com.est.runtime.signup.repository.MemberRepository;
import com.est.runtime.signup.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        boolean exists = memberRepository.findByUsername(username).isPresent();

        Map<String, Object> response = Map.of(
                "exists", exists,
                "message", exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다."
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkNickname(@RequestParam String nickname) {
        boolean exists = memberRepository.findByNickname(nickname).isPresent();

        Map<String, Object> response = Map.of(
                "exists", exists,
                "message", exists ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다."
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete-user")
    public ResponseEntity<MemberUpdateResponse> deleteUser(@RequestBody MemberDeleteDTO deleteRequest) {
        MemberUpdateResponse res = memberService.deleteUser(deleteRequest);
        return new ResponseEntity<>(res, res.getStatusCode());
    }

    @PostMapping("/update-user")
    public ResponseEntity<MemberUpdateResponse> updateUser(@RequestBody MemberInfoUpdateDTO updateRequest) {
        MemberUpdateResponse res = memberService.updateUser(updateRequest);
        return new ResponseEntity<>(res, res.getStatusCode());
    }
}
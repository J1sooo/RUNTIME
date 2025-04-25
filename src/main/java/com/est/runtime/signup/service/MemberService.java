package com.est.runtime.signup.service;

import com.est.runtime.signup.dto.MemberDTO;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.entity.UserLevel;
import com.est.runtime.signup.repository.MemberRepository;
import com.est.runtime.signup.repository.UserLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserLevelRepository userLevelRepository;

    public void save(MemberDTO memberDTO) {
        // 1. DTO → Entity 변환
        Member member = Member.toEntity(memberDTO);

        // 2. 기본 로그인 방식 설정 (Auth 구현시 필요)
        member.setFormLogin(true);

        // 3. 기본 등급 설정 (ex. 브론즈 등급) 가입시 디폴트로 최하위 등급으로 배정
        UserLevel defaultLevel = userLevelRepository.findByLevelNumber(1);
        member.setLevel(defaultLevel); // 혹은 디폴트 level 지정

        // 4. 저장
        memberRepository.save(member);
    }
}
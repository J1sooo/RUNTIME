package com.est.runtime;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.est.runtime.signup.dto.MemberDTO;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.repository.MemberRepository;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void prepareMemberList() {
        memberRepository.save(Member.toEntity(new MemberDTO(0L, "username1", "password1", "nickname1")));
        memberRepository.save(Member.toEntity(new MemberDTO(1L, "username2", "password2", "nickname2")));
        memberRepository.save(Member.toEntity(new MemberDTO(2L, "username3", "password3", "nickname3")));
    }

    @Test
    public void getNumOfMembersTest() {
        Assertions.assertThat(memberRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    public void findByUsernameTest() {
        Optional<Member> memberQuery1 = memberRepository.findByUsername("username1");
        Optional<Member> memberQuery2 = memberRepository.findByUsername("username2");
        Optional<Member> memberQuery3 = memberRepository.findByUsername("username3");

        Assertions.assertThat(memberQuery1.isPresent()).isTrue();
        Assertions.assertThat(memberQuery1.get().getUsername()).isEqualTo("username1");
        Assertions.assertThat(memberQuery2.isPresent()).isTrue();
        Assertions.assertThat(memberQuery2.get().getUsername()).isEqualTo("username2");
        Assertions.assertThat(memberQuery3.isPresent()).isTrue();
        Assertions.assertThat(memberQuery3.get().getUsername()).isEqualTo("username3");
    }

    @Test
    public void findByNicknameTest() {
        Optional<Member> memberQuery1 = memberRepository.findByNickname("nickname1");
        Optional<Member> memberQuery2 = memberRepository.findByNickname("nickname2");
        Optional<Member> memberQuery3 = memberRepository.findByNickname("nickname3");

        Assertions.assertThat(memberQuery1.isPresent()).isTrue();
        Assertions.assertThat(memberQuery1.get().getNickname()).isEqualTo("nickname1");
        Assertions.assertThat(memberQuery2.isPresent()).isTrue();
        Assertions.assertThat(memberQuery2.get().getNickname()).isEqualTo("nickname2");
        Assertions.assertThat(memberQuery3.isPresent()).isTrue();
        Assertions.assertThat(memberQuery3.get().getNickname()).isEqualTo("nickname3");
    }

}

package com.est.runtime;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.est.runtime.post.like.PostLikeRepository;
import com.est.runtime.signup.dto.MemberDTO;
import com.est.runtime.signup.entity.UserLevel;
import com.est.runtime.signup.repository.AccessAuthorityRepository;
import com.est.runtime.signup.repository.AuthorityForLevelRepository;
import com.est.runtime.signup.repository.LoginRequestLogRepository;
import com.est.runtime.signup.repository.MemberRepository;
import com.est.runtime.signup.repository.UserLevelRepository;
import com.est.runtime.signup.service.MemberService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private UserLevelRepository userLevelRepository;
    @Mock
    private AccessAuthorityRepository accessAuthorityRepository;
    @Mock
    private AuthorityForLevelRepository authorityForLevelRepository;
    @Mock
    private LoginRequestLogRepository loginRequestLogRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private PostLikeRepository postLikeRepository;

    @Test
    public void registrationTest() {
        MemberDTO dto = new MemberDTO(0L, "username", "password", "nickname");
        Mockito.when(userLevelRepository.save(any())).thenReturn(UserLevel.builder().id(1L).levelNumber(1).levelName("BRONZE").build());
        memberService.save(dto);
        Mockito.verify(memberRepository, Mockito.times(1)).save(any());
    }
}

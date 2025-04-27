package com.est.runtime.signup.service;

import com.est.runtime.signup.dto.MemberDTO;
import com.est.runtime.signup.entity.AccessAuthority;
import com.est.runtime.signup.entity.AuthorityForLevel;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.entity.UserLevel;
import com.est.runtime.signup.repository.AccessAuthorityRepository;
import com.est.runtime.signup.repository.AuthorityForLevelRepository;
import com.est.runtime.signup.repository.MemberRepository;
import com.est.runtime.signup.repository.UserLevelRepository;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final UserLevelRepository userLevelRepository;
    private final AccessAuthorityRepository accessAuthorityRepository;
    private final AuthorityForLevelRepository authorityForLevelRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void save(MemberDTO memberDTO) {
        // 1. DTO → Entity 변환
        Member member = Member.toEntity(memberDTO);

        // 1-1. Encrypt the password
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        // 2. 기본 로그인 방식 설정 (Auth 구현시 필요)
        member.setFormLogin(true);

        // 3. 기본 등급 설정 (ex. 브론즈 등급) 가입시 디폴트로 최하위 등급으로 배정
        UserLevel defaultLevel = getDefaultLevel();
        member.setLevel(defaultLevel);

        // 4. 저장
        memberRepository.save(member);
    }

    /* 
     * As a test, a default(base) user level with the following configurations will be made:
     * Level name: BRONZE
     * Level number: 1
     * Granted authorities: allowed to access /post with the "board" parameter set to "general"
     * 
     */
    private UserLevel getDefaultLevel() {
        Optional<UserLevel> defaultLevelQuery = userLevelRepository.findByLevelNumber(1);
        if (defaultLevelQuery.isPresent()) {
            return defaultLevelQuery.get();
        }
        
        UserLevel lvl = userLevelRepository.save(UserLevel.builder().levelNumber(1).levelName("BRONZE").build()); //Default level is BRONZE
        addAuthorityForUserLevel("GET_BOARD_GENERAL", "Authority to read from a board named 'General", lvl);
        addAuthorityForUserLevel("POST_BOARD_GENERAL", "Authority to post to a board named 'General", lvl);
        addAuthorityForUserLevel("PUT_BOARD_GENERAL", "Authority to edit contents on a board named 'General", lvl);
        addAuthorityForUserLevel("DELETE_BOARD_GENERAL", "Authority to delete contents on a board named 'General", lvl);
        return lvl;
    }

    /* 
     * This function adds authority for a user level 
     * As of now, the naming rule for authority regarding board access is as follows:
     * "(HTTP Method name)_BOARD_(Board name)"
     * 
     * For example, in order to have the authority to make GET requests to a board called "general", the authority "GET_BOARD_GENERAL" is needed.
     * Similarly, in order to make POST requests to the same board, the authority "POST_BOARD_GENERAL" would be needed.
     * 
     */
    public void addAuthorityForUserLevel(String authorityName, String description, UserLevel l) {
        for (AuthorityForLevel al: l.getAuthorities()) {
            if (al.getAuthority().getName().equalsIgnoreCase(authorityName)) {
                return;
            }
        }
        Optional<AccessAuthority> authorityOptional = accessAuthorityRepository.findByName(authorityName);
        if (authorityOptional.isPresent()) {
            authorityForLevelRepository.save(AuthorityForLevel.builder().authority(authorityOptional.get()).userLevel(l).build());
            return;
        }
        AccessAuthority a = accessAuthorityRepository.save(AccessAuthority.builder().name(authorityName).displayName(description).build());
        authorityForLevelRepository.save(AuthorityForLevel.builder().authority(a).userLevel(l).build());
        return;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException(new StringBuilder().append("Provided username: \"").append(username).append("\" does not exist!").toString());
        });
    }
}
package com.est.runtime.signup.service;

import com.est.runtime.post.like.PostLikeRepository;
import com.est.runtime.signup.dto.*;
import com.est.runtime.signup.entity.*;
import com.est.runtime.signup.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final UserLevelRepository userLevelRepository;
    private final AccessAuthorityRepository accessAuthorityRepository;
    private final AuthorityForLevelRepository authorityForLevelRepository;
    private final LoginRequestLogRepository loginRequestLogRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PostLikeRepository postLikeRepository;


    public void save(MemberDTO memberDTO) {
        Member member = Member.toEntity(memberDTO);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setFormLogin(true);
        UserLevel defaultLevel = getDefaultLevel();
        member.setLevel(defaultLevel);
        memberRepository.save(member);
    }

    public MemberDTO findByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
        return new MemberDTO(null, member.getUsername(), null, member.getNickname());
    }

    public MemberLoginStatus isLoggedIn() {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth != null && currentAuth.getPrincipal() instanceof Member currMember) {
            return MemberLoginStatus.builder()
                    .nickname(currMember.getNickname())
                    .username(currMember.getUsername())
                    .id(currMember.getId())
                    .loggedIn(true)
                    .statusCode(HttpStatus.OK)
                    .build();
        }
        return MemberLoginStatus.builder()
                .nickname("")
                .username("")
                .loggedIn(false)
                .id(-1L)
                .statusCode(HttpStatus.OK)
                .build();
    }

    private UserLevel getDefaultLevel() {
        Optional<UserLevel> defaultLevelQuery = userLevelRepository.findByLevelNumber(1);
        if (defaultLevelQuery.isPresent()) {
            return defaultLevelQuery.get();
        }

        UserLevel lvl = userLevelRepository.save(UserLevel.builder().levelNumber(1).levelName("BRONZE").build());
        addAuthorityForUserLevel("GET_BOARD_GENERAL", "Authority to read from a board named 'General'", lvl);
        addAuthorityForUserLevel("POST_BOARD_GENERAL", "Authority to post to a board named 'General'", lvl);
        addAuthorityForUserLevel("PUT_BOARD_GENERAL", "Authority to edit contents on a board named 'General'", lvl);
        addAuthorityForUserLevel("DELETE_BOARD_GENERAL", "Authority to delete contents on a board named 'General'", lvl);
        return lvl;
    }

    public void addAuthorityForUserLevel(String authorityName, String description, UserLevel l) {
        for (AuthorityForLevel al : l.getAuthorities()) {
            if (al.getAuthority().getName().equalsIgnoreCase(authorityName)) {
                return;
            }
        }
        Optional<AccessAuthority> authorityOptional = accessAuthorityRepository.findByName(authorityName);
        if (authorityOptional.isPresent()) {
            authorityForLevelRepository.save(
                    AuthorityForLevel.builder().authority(authorityOptional.get()).userLevel(l).build());
            return;
        }
        AccessAuthority a = accessAuthorityRepository.save(
                AccessAuthority.builder().name(authorityName).displayName(description).build());
        authorityForLevelRepository.save(AuthorityForLevel.builder().authority(a).userLevel(l).build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Provided username: \"" + username + "\" does not exist!"));
    }

    public MemberUpdateResponse updateUser(MemberInfoUpdateDTO memberDto) {
        Optional<Member> memberQuery = memberRepository.findByUsername(memberDto.getUsername());
        if (memberQuery.isPresent()) {
            Member memberToUpdate = memberQuery.get();
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Member currentMember) {
                if (memberToUpdate.getUsername().equals(currentMember.getUsername()) &&
                        passwordEncoder.matches(memberDto.getPassword(), memberToUpdate.getPassword())) {
                    StringBuilder res = new StringBuilder("Successfully updated the account's ");
                    if (memberDto.getNewNickname() != null && !memberDto.getNewNickname().isEmpty()) {
                        memberToUpdate.setNickname(memberDto.getNewNickname());
                        res.append("nickname ");
                    }
                    if (memberDto.getNewUsername() != null && !memberDto.getNewUsername().isEmpty()) {
                        memberToUpdate.setUsername(memberDto.getNewUsername());
                        res.append("username ");
                    }
                    if (memberDto.getNewPassword() != null && !memberDto.getNewPassword().isEmpty()) {
                        memberToUpdate.setPassword(passwordEncoder.encode(memberDto.getNewPassword()));
                        res.append("password ");
                    }
                    memberToUpdate = memberRepository.save(memberToUpdate);
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                            memberToUpdate,
                            SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                            memberToUpdate.getAuthorities()
                    ));
                    return MemberUpdateResponse.builder()
                            .statusCode(HttpStatus.OK)
                            .message(res.append("data.").toString())
                            .username(memberDto.getUsername())
                            .build();
                }
                return MemberUpdateResponse.builder()
                        .message("Update failed. Password incorrect or unauthorized user.")
                        .username(memberDto.getUsername())
                        .statusCode(HttpStatus.FORBIDDEN)
                        .build();
            }
            return MemberUpdateResponse.builder()
                    .message("Update failed. Authorization not valid.")
                    .username(memberDto.getUsername())
                    .statusCode(HttpStatus.FORBIDDEN)
                    .build();
        }
        return MemberUpdateResponse.builder()
                .message("Update failed. Username not found.")
                .username(memberDto.getUsername())
                .statusCode(HttpStatus.NOT_FOUND)
                .build();
    }

    @Transactional
    public MemberUpdateResponse deleteUser(MemberDeleteDTO memberDto) {
        Optional<Member> memberQuery = memberRepository.findByUsername(memberDto.getUsername());
        if (memberQuery.isPresent()) {
            Member memberToDelete = memberQuery.get();
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Member currentMember) {
                if (memberToDelete.getUsername().equals(currentMember.getUsername()) &&
                        passwordEncoder.matches(memberDto.getPassword(), memberToDelete.getPassword())) {

                    // 로그인 요청 로그 먼저 삭제
                    loginRequestLogRepository.deleteByMember(memberToDelete);

                    //로그인 좋아요 로그 삭제
                    postLikeRepository.deleteByMember(memberToDelete);

                    // 회원 삭제
                    memberRepository.deleteById(memberToDelete.getId());

                    return MemberUpdateResponse.builder()
                            .message("Delete successful")
                            .username(memberDto.getUsername())
                            .statusCode(HttpStatus.OK)
                            .build();
                }
                return MemberUpdateResponse.builder()
                        .message("Delete failed. Incorrect password or unauthorized.")
                        .username(memberDto.getUsername())
                        .statusCode(HttpStatus.FORBIDDEN)
                        .build();
            }
            return MemberUpdateResponse.builder()
                    .message("Delete failed. Authorization not valid.")
                    .username(memberDto.getUsername())
                    .statusCode(HttpStatus.FORBIDDEN)
                    .build();
        }
        return MemberUpdateResponse.builder()
                .message("Delete failed. Username not found.")
                .username(memberDto.getUsername())
                .statusCode(HttpStatus.NOT_FOUND)
                .build();
    }
}
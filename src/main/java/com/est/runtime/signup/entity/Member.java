package com.est.runtime.signup.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.est.runtime.signup.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "member_table")
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; // 기존 memberEmail → 로그인 시 필요한 username으로 통일

    @Column(nullable = false)
    private String password; // 기존 memberPassword

    @Column(nullable = false)
    private String nickname; // 기존 memberName → nickname으로 통일

    @Column(nullable = false)
    private boolean formLogin = true; // 기본 로그인 방식 설정

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_level")
    private UserLevel level;

    public static Member toEntity(MemberDTO dto) {
        Member member = new Member();
        member.setUsername(dto.getUsername());
        member.setPassword(dto.getPassword());
        member.setNickname(dto.getNickname());
        return member;
    }
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<SimpleGrantedAuthority> hs = new HashSet<>();
        level.getAuthorities().forEach(x -> hs.add(new SimpleGrantedAuthority(x.getAuthority().getName())));
        return Collections.unmodifiableSet(hs);
    }
}
package com.est.runtime.signup.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.est.runtime.comment.Comment;
import com.est.runtime.post.Post;
import com.est.runtime.post.like.PostLike;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
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
@EntityListeners(AuditingEntityListener.class)
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    private boolean isAdmin = false;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private LocalDateTime joinDate;


    @ManyToOne
    @JoinColumn(name = "user_level")
    private UserLevel level;

    private Long loginCount = 0L;
    private Long consecutiveFailedLoginAttempts = 0L;

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
    public Long incrementLoginCount() {
        return ++loginCount;
    }
    public Long incrementFailureCount() {
        return ++consecutiveFailedLoginAttempts;
    }
    public void resetFailureCount() {
        this.consecutiveFailedLoginAttempts = 0L;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<SimpleGrantedAuthority> hs = new HashSet<>();
        if (isAdmin) {
            hs.add(new SimpleGrantedAuthority("RUNTIME_ADMIN"));
        }
        level.getAuthorities().forEach(x -> hs.add(new SimpleGrantedAuthority(x.getAuthority().getName())));
        return Collections.unmodifiableSet(hs);
    }
}
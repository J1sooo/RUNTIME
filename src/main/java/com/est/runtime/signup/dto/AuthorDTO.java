package com.est.runtime.signup.dto;

import com.est.runtime.signup.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorDTO {
    private Long id;
    private String nickname;

    public AuthorDTO(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}
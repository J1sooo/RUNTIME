package com.est.runtime.signup.dto;

import com.est.runtime.signup.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorDTO {
    private Long id;
    private String username;
    private String nickname;
    private String levelName;
    private Integer levelNumber;

    public AuthorDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.levelName = member.getLevel().getLevelName();     // null 체크 불필요
        this.levelNumber = member.getLevel().getLevelNumber();

    }
}
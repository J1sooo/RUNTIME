package com.est.runtime.signup.dto;

import com.est.runtime.signup.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberForNoteDTO {
    private Long id;

    private String username;
    private String nickname;

    public MemberForNoteDTO(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.username = member.getUsername();
    }
}
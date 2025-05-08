package com.est.runtime.signup.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
    private Long id;

    private String username;
    private String password;
    private String nickname;


}
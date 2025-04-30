package com.est.runtime.signup.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MemberLoginStatus {
    private final boolean loggedIn;
    private final String username;
    private final String nickname;
    private final HttpStatus statusCode;
}

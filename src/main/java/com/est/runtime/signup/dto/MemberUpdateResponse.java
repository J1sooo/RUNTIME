package com.est.runtime.signup.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MemberUpdateResponse {
    private final String username;
    private final String message;
    private final HttpStatus statusCode;
}

package com.est.runtime.signup.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FollowApiFollowResponse {
    private final String message;
    private final HttpStatus statusCode;
    private final FollowInfoDTO followInfo;
}

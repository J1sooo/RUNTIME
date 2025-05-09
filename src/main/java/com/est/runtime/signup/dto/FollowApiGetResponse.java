package com.est.runtime.signup.dto;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FollowApiGetResponse {
    private final String message;
    private final HttpStatus statusCode;
    private final List<FollowInfoDTO> followInfoList;
}

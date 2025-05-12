package com.est.runtime.admin.dto.response;


import org.springframework.http.HttpStatus;

import com.est.runtime.admin.dto.UserLevelInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminCreateUserLevelResponse {
    private final UserLevelInfo level;
    private final String message;
    private final HttpStatus responseCode;
}

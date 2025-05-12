package com.est.runtime.admin.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.est.runtime.admin.dto.UserLevelInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminGetUserLevelResponse {
    private final List<UserLevelInfo> levels;
    private final HttpStatus responseCode;
    private final String message;
}

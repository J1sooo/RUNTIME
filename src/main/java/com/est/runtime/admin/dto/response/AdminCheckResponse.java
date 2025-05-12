package com.est.runtime.admin.dto.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminCheckResponse {
    private final HttpStatus responseCode;
    private final boolean isAdmin;
    private final String message;
}

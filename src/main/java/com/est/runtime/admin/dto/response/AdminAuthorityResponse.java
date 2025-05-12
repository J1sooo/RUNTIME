package com.est.runtime.admin.dto.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminAuthorityResponse {
    private final String message;
    private final HttpStatus responseCode;
}

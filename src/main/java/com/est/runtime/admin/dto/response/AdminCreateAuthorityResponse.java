package com.est.runtime.admin.dto.response;

import org.springframework.http.HttpStatus;

import com.est.runtime.admin.dto.AuthorityInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminCreateAuthorityResponse {
    private final AuthorityInfo authority;
    private final String message;
    private final HttpStatus responseCode;
}

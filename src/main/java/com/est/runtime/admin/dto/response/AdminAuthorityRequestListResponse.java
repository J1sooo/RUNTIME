package com.est.runtime.admin.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.est.runtime.admin.dto.AuthorityRequestInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminAuthorityRequestListResponse {
    private final List<AuthorityRequestInfo> requests;
    private final String message;
    private final HttpStatus responseCode;
}

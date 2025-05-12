package com.est.runtime.admin.dto.response;

import org.springframework.http.HttpStatus;

import com.est.runtime.admin.dto.MemberInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminUpdateMemberResponse {
    private final MemberInfo member;
    private final String message;
    private final HttpStatus responseCode;
}

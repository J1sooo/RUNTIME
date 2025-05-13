package com.est.runtime.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AuthorityRequestInfo {
    private final Long requestId;
    private final Long requestMemberId;
    private final Long requestTime;
    private final String challengeCode;
}

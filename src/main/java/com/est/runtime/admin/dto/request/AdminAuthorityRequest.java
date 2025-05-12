package com.est.runtime.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminAuthorityRequest {
    @JsonProperty("member_id")
    private final Long memberId;
    @JsonProperty("challenge")
    private final String challenge;

    public AdminAuthorityRequest() {
        this.memberId = -1L;
        this.challenge = "";
    }
}

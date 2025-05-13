package com.est.runtime.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AdminUpdateMemberLevelRequest {
    @JsonProperty("member_id")
    private final Long memberId;
    @JsonProperty("level")
    private final Integer levelNum;

    public AdminUpdateMemberLevelRequest() {
        this.memberId = -1L;
        this.levelNum = -1;
    }
}

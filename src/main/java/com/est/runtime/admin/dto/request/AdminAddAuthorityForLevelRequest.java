package com.est.runtime.admin.dto.request;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminAddAuthorityForLevelRequest {
    @JsonProperty("level_number")
    private final Integer levelNumber;
    @JsonProperty("authority_name")
    @Nullable
    private final String authorityName;
    @JsonProperty("authority_id")
    @Nullable
    private final Long authorityId;

    public AdminAddAuthorityForLevelRequest() {
        this.levelNumber = -1;
        this.authorityId = -1L;
        this.authorityName = "";
    }
}

package com.est.runtime.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminCreateUserLevelRequest {
    @JsonProperty("level_number")
    private final Integer levelNumber;
    @JsonProperty("level_name")
    private final String levelName;

    public AdminCreateUserLevelRequest() {
        this.levelNumber = -1;
        this.levelName = "";
    }
}

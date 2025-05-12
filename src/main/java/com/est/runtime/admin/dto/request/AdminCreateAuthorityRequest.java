package com.est.runtime.admin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminCreateAuthorityRequest {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("description")
    private final String description;

    public AdminCreateAuthorityRequest() {
        this.name = "";
        this.description = "";
    }
}

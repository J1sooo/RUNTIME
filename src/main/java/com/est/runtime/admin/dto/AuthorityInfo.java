package com.est.runtime.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AuthorityInfo {
    private final Long id;
    private final String name;
    private final String description;
}

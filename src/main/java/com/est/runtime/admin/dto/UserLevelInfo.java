package com.est.runtime.admin.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserLevelInfo {
    private final Long id;
    private final List<AuthorityInfo> authorities;
    private final Integer levelNo;
    private final String levelName;

    protected UserLevelInfo() {
        this.id = -1L;
        this.authorities = List.of();
        this.levelNo = -1;
        this.levelName = "";
    }

    public static UserLevelInfo emptyInstance() {
        return new UserLevelInfo();
    }
}

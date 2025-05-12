package com.est.runtime.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MemberInfo {
    private final Long id;
    private final String nickname;
    private final String username;
    private final Integer levelNo;
}

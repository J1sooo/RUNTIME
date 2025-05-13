package com.est.runtime.admin.dto;

import java.time.LocalDateTime;

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
    private final LocalDateTime joinDate;

    public static MemberInfo emptyInstance() {
        return MemberInfo.builder().id(-1L).levelNo(-1).nickname("").username("").joinDate(LocalDateTime.now()).build();
    }
}

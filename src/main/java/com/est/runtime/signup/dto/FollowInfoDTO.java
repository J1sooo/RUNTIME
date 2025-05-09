package com.est.runtime.signup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FollowInfoDTO {
    private final String follower;
    private final String followTarget;
    private final Long followerId;
    private final Long followTargetId;
}

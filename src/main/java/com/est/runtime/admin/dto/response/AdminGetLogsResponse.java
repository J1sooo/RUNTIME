package com.est.runtime.admin.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.est.runtime.admin.dto.LogInfo;
import com.est.runtime.admin.dto.MemberInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AdminGetLogsResponse {
    private final List<LogInfo> logs;
    private final MemberInfo member;
    private final Long currentLoginCount;
    private final Long currentConsecutiveFailedLoginCount;
    private final LocalDateTime joinDate;
    private final HttpStatus responseCode;
    private final String message;
}
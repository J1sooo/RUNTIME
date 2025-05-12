package com.est.runtime.admin.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class LogInfo {
    private final String userAgent;
    private final String ipAddress;
    private final boolean isSuccessful;
    private LocalDateTime timeStamp;
}
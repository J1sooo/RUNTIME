package com.est.runtime.signup.dto;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoUpdateDTO {
    @JsonProperty("username")
    private final String username;
    @JsonProperty("password")
    private final String password;
    @Nullable
    @JsonProperty("new_username")
    private final String newUsername;
    @Nullable
    @JsonProperty("new_password")
    private final String newPassword;
    @Nullable
    @JsonProperty("new_nickname")
    private final String newNickname;
}
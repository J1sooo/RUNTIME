package com.est.runtime.admin.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.est.runtime.admin.dto.MemberInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;

@AllArgsConstructor
@Getter
@Builder
public class AdminGetMembersResponse {
    private final List<MemberInfo> members;
    private final String message;
    private final HttpStatus responseCode;

    public Integer getNumberOfMembers() {
        return members.size();
    }
}

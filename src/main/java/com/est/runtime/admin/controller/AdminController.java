package com.est.runtime.admin.controller;

import org.springframework.web.bind.annotation.RestController;

import com.est.runtime.admin.dto.request.AdminAddAuthorityForLevelRequest;
import com.est.runtime.admin.dto.request.AdminAuthorityRequest;
import com.est.runtime.admin.dto.request.AdminCreateAuthorityRequest;
import com.est.runtime.admin.dto.request.AdminCreateUserLevelRequest;
import com.est.runtime.admin.dto.request.AdminUpdateMemberLevelRequest;
import com.est.runtime.admin.dto.response.AdminAuthorityResponse;
import com.est.runtime.admin.dto.response.AdminCheckResponse;
import com.est.runtime.admin.dto.response.AdminCreateAuthorityResponse;
import com.est.runtime.admin.dto.response.AdminCreateUserLevelResponse;
import com.est.runtime.admin.dto.response.AdminGetLogsResponse;
import com.est.runtime.admin.dto.response.AdminGetMembersResponse;
import com.est.runtime.admin.dto.response.AdminGetUserLevelResponse;
import com.est.runtime.admin.dto.response.AdminUpdateMemberResponse;
import com.est.runtime.admin.service.AdminService;
import com.est.runtime.signup.entity.Member;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/api/admin/get-members")
    public ResponseEntity<AdminGetMembersResponse> getMembers(@RequestParam(name = "id", required = false, defaultValue = "-1") Long memberId,
        @RequestParam(name = "username", required = false, defaultValue = "") String memberUsername,
        @RequestParam(name = "nickname", required = false, defaultValue = "") String memberNickname) {
        if (memberId == -1L && memberUsername.isEmpty() && memberNickname.isEmpty()) {
            AdminGetMembersResponse res = adminService.getAllMembers();
            return ResponseEntity.status(res.getResponseCode()).body(res);
        }
        if (memberId != -1L) {
            AdminGetMembersResponse res = adminService.getMemberById(memberId);
            return ResponseEntity.status(res.getResponseCode()).body(res);
        }
        AdminGetMembersResponse res = adminService.getMembersContainingNicknameAndUsername(memberNickname, memberUsername);
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }

    @GetMapping("/api/admin/get-logs")
    public ResponseEntity<AdminGetLogsResponse> getLogsForMember(@RequestParam(name = "id", required = false, defaultValue = "-1") Long memberId,
        @RequestParam(name = "before", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime before,
        @RequestParam(name = "after", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime after) {
        AdminGetLogsResponse res = adminService.getUserLogs(memberId, before, after);
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }

    @GetMapping("/api/admin/get-levels")
    public ResponseEntity<AdminGetUserLevelResponse> getLevels() {
        AdminGetUserLevelResponse res = adminService.getUserLevels();
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }

    @PostMapping("/api/admin/request-admin")
    public ResponseEntity<AdminAuthorityResponse> requestAdmin(@RequestBody AdminAuthorityRequest request) {
        AdminAuthorityResponse res = adminService.handleAuthorityRequest(request);
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }

    @PostMapping("/api/admin/create-authority")
    public ResponseEntity<AdminCreateAuthorityResponse> createAuthority(@RequestBody AdminCreateAuthorityRequest request) {
        AdminCreateAuthorityResponse res = adminService.createAuthority(request);
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }

    @PostMapping("/api/admin/add-authority-for-level")
    public ResponseEntity<AdminCreateUserLevelResponse> createAuthority(@RequestBody AdminAddAuthorityForLevelRequest request) {
        AdminCreateUserLevelResponse res = adminService.addAuthorityForUserLevel(request);
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }

    @PostMapping("/api/admin/remove-authority-for-level")
    public ResponseEntity<AdminCreateUserLevelResponse> removeAuthority(@RequestBody AdminAddAuthorityForLevelRequest request) {
        AdminCreateUserLevelResponse res = adminService.removeAuthorityForLevel(request);
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }

    @PostMapping("/api/admin/create-user-level")
    public ResponseEntity<AdminCreateUserLevelResponse> createUserLevel (@RequestBody AdminCreateUserLevelRequest request) {
        AdminCreateUserLevelResponse res = adminService.createNewLevel(request);
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }
    

    @PostMapping("/api/admin/change-user-level")
    public ResponseEntity<AdminUpdateMemberResponse> changeUserLevel(@RequestBody AdminUpdateMemberLevelRequest request) {
        AdminUpdateMemberResponse res = adminService.updateMemberLevel(request);
        return ResponseEntity.status(res.getResponseCode()).body(res);
    }
    

    @GetMapping("/api/admin/is-admin")
    public ResponseEntity<AdminCheckResponse> getMethodName(@AuthenticationPrincipal Member member) {
        for(GrantedAuthority ga: member.getAuthorities()) {
            if (ga.getAuthority().equalsIgnoreCase("RUNTIME_ADMIN")) {
                return ResponseEntity.ok().body(AdminCheckResponse.builder().
                    responseCode(HttpStatus.OK).
                    message("You currently have the admin authority").
                    isAdmin(true).build());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AdminCheckResponse.builder().
            responseCode(HttpStatus.FORBIDDEN).
            message("You currently DO NOT have the admin authority").
            isAdmin(false).build());
    }
}

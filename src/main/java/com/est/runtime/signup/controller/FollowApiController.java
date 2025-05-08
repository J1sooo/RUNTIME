package com.est.runtime.signup.controller;

import org.springframework.web.bind.annotation.RestController;

import com.est.runtime.signup.dto.FollowApiFollowResponse;
import com.est.runtime.signup.dto.FollowApiGetResponse;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.service.FollowService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
public class FollowApiController {
    private final FollowService followService;
    @PostMapping("/api/follow")
    public ResponseEntity<FollowApiFollowResponse> setFollow(@RequestParam(name = "follow-target") Long followTarget, @AuthenticationPrincipal Member member) {
        FollowApiFollowResponse res = followService.follow(member, followTarget);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
    @PostMapping("/api/unfollow")
    public ResponseEntity<FollowApiFollowResponse> setUnfollow(@RequestParam(name = "follow-target") Long followTarget, @AuthenticationPrincipal Member member) {
        FollowApiFollowResponse res = followService.unfollow(member, followTarget);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @GetMapping("/api/followers")
    public ResponseEntity<FollowApiGetResponse> getFollowerList(@RequestParam(name = "user") Long user) {
        FollowApiGetResponse res = followService.getFollowerList(user);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

    @GetMapping("/api/following")
    public ResponseEntity<FollowApiGetResponse> getFollowingList(@RequestParam(name = "user") Long user) {
        FollowApiGetResponse res = followService.getFollowingList(user);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }
}
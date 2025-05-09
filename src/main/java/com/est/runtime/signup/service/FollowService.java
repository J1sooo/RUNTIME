package com.est.runtime.signup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.est.runtime.signup.dto.FollowApiFollowResponse;
import com.est.runtime.signup.dto.FollowApiGetResponse;
import com.est.runtime.signup.dto.FollowInfoDTO;
import com.est.runtime.signup.entity.FollowInfo;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.repository.FollowInfoRepository;
import com.est.runtime.signup.repository.MemberRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FollowService {
    private final MemberRepository memberRepository;
    private final FollowInfoRepository followInfoRepository;


    public FollowApiFollowResponse follow(Member follower, Long followTarget) {
        Optional<Member> memberQuery = memberRepository.findById(followTarget);
        if (memberQuery.isEmpty()) {
            return FollowApiFollowResponse.builder().
                message("The requested follow target member ID is invalid!").
                statusCode(HttpStatus.NOT_FOUND).
                followInfo(new FollowInfoDTO(follower.getUsername(), "",
                follower.getId(), -1L)).build();
        }
        return follow(follower, memberQuery.get());
    }

    public FollowApiFollowResponse unfollow(Member follower, Long followTarget) {
        Optional<Member> memberQuery = memberRepository.findById(followTarget);
        if (memberQuery.isEmpty()) {
            return FollowApiFollowResponse.builder().
                message("The requested follow target member ID is invalid!").
                statusCode(HttpStatus.NOT_FOUND).
                followInfo(new FollowInfoDTO(follower.getUsername(), "",
                follower.getId(), -1L)).build();
        }
        return unfollow(follower, memberQuery.get());
    }

    @Transactional
    public FollowApiFollowResponse follow(Member follower, Member followTarget) {
        for (FollowInfo f: followInfoRepository.getFollowersForMember(followTarget.getId())) {
            if (f.getFollower().getId() == follower.getId()) {
                return FollowApiFollowResponse.builder().
                    message("The target member is already being followed by the follower member!").
                    statusCode(HttpStatus.CONFLICT).
                    followInfo(new FollowInfoDTO(follower.getUsername(), followTarget.getUsername(),
                         follower.getId(), followTarget.getId())).build();
            }
        }
        followInfoRepository.save(FollowInfo.builder().
            follower(follower).
            followTarget(followTarget).build());
        return FollowApiFollowResponse.builder().
            message("New follow information added!").
            statusCode(HttpStatus.OK).
            followInfo(new FollowInfoDTO(follower.getUsername(), followTarget.getUsername(),
                 follower.getId(), followTarget.getId())).build();
    }

    @Transactional
    public FollowApiFollowResponse unfollow(Member follower, Member followTarget) {
        for (FollowInfo f: followInfoRepository.getFollowersForMember(followTarget.getId())) {
            if (f.getFollower().getId() == follower.getId()) {
                followInfoRepository.delete(f);
                return FollowApiFollowResponse.builder().
                    message("Follow information deleted!").
                    statusCode(HttpStatus.OK).
                    followInfo(new FollowInfoDTO(follower.getUsername(), followTarget.getUsername(),
                        follower.getId(), followTarget.getId())).build();
            }
        }
        return FollowApiFollowResponse.builder().
            message("The follower member is not following the follow target member!").
            statusCode(HttpStatus.BAD_REQUEST).
            followInfo(new FollowInfoDTO(follower.getUsername(), followTarget.getUsername(),
            follower.getId(), followTarget.getId())).build();
    }

    public FollowApiGetResponse getFollowerList(Long memberId) {
        Optional<Member> memberQuery = memberRepository.findById(memberId);
        if (memberQuery.isEmpty()) {
            return FollowApiGetResponse.builder().
                message("The requested member ID is invalid!").
                statusCode(HttpStatus.NOT_FOUND).
                followInfoList(List.of()).build();
        }
        List<FollowInfoDTO> fi = followInfoRepository.getFollowersForMember(memberId).stream().map(x -> FollowInfoDTO.builder().
            follower(x.getFollower().getUsername()).
            followerId(x.getFollower().getId()).
            followTarget(x.getFollowTarget().getUsername()).
            followTargetId(x.getFollowTarget().getId()).build()).toList();
        return FollowApiGetResponse.builder().
            message("This is the list of users that are following this user.").
            statusCode(HttpStatus.OK).
            followInfoList(fi).build();
    }

    public FollowApiGetResponse getFollowingList(Long memberId) {
        Optional<Member> memberQuery = memberRepository.findById(memberId);
        if (memberQuery.isEmpty()) {
            return FollowApiGetResponse.builder().
                message("The requested member ID is invalid!").
                statusCode(HttpStatus.NOT_FOUND).
                followInfoList(List.of()).build();
        }
        List<FollowInfoDTO> fi = followInfoRepository.getMembersFollowingList(memberId).stream().map(x -> FollowInfoDTO.builder().
            follower(x.getFollower().getUsername()).
            followerId(x.getFollower().getId()).
            followTarget(x.getFollowTarget().getUsername()).
            followTargetId(x.getFollowTarget().getId()).build()).toList();
        return FollowApiGetResponse.builder().
            message("This is the list of users that this user is currentl following.").
            statusCode(HttpStatus.OK).
            followInfoList(fi).build();
    }
}

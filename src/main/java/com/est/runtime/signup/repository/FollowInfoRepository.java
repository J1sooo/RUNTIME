package com.est.runtime.signup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.est.runtime.signup.entity.FollowInfo;

public interface FollowInfoRepository extends JpaRepository<FollowInfo, Long> {
    @Query(value = "SELECT f FROM FollowInfo f INNER JOIN Member m ON f.followTarget.id = m.id WHERE m.id = :memberId")
    List<FollowInfo> getFollowersForMember(@Param("memberId") Long memberId);
    @Query(value = "SELECT f FROM FollowInfo f INNER JOIN Member m ON f.follower.id = m.id WHERE m.id = :memberId")
    List<FollowInfo> getMembersFollowingList(@Param("memberId") Long memberId);
    @Query(value = "SELECT COUNT(f) FROM FollowInfo f INNER JOIN Member m ON f.followTarget.id = m.id WHERE m.id = :memberId")
    Long getFollowerCountForMember(@Param("memberId") Long memberId);
    @Query(value = "SELECT COUNT(f) FROM FollowInfo f INNER JOIN Member m ON f.follower.id = m.id WHERE m.id = :memberId")
    Long getFollowingCountForMember(@Param("memberId") Long memberId);
}
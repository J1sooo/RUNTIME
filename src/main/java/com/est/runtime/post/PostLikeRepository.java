package com.est.runtime.post;

import com.est.runtime.signup.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // 특정 회원이 특정 게시글에 누른 좋아요 조회
    Optional<PostLike> findByPostIdAndMemberId(Long postId, Long memberId);

    // 특정 회원이 누른 모든 좋아요 삭제 메서드
    void deleteByMember(Member member);
}
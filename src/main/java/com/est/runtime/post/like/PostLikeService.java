package com.est.runtime.post.like;

import com.est.runtime.post.Post;
import com.est.runtime.post.PostRepository;
import com.est.runtime.post.like.dto.LikeResponseDTO;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public LikeResponseDTO toggleLike(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        boolean liked;
        var existing = postLikeRepository.findByPostIdAndMemberId(postId, memberId);
        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get());
            post.setLikes(post.getLikes() - 1);
            liked = false;
        } else {
            postLikeRepository.save(new PostLike(post, member));
            post.setLikes(post.getLikes() + 1);
            liked = true;
        }

        return new LikeResponseDTO(liked, post.getLikes());
    }
}
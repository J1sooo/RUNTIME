package com.est.runtime.comment;

import com.est.runtime.comment.dto.CommentRequest;
import com.est.runtime.post.Post;
import com.est.runtime.post.PostRepository;
import com.est.runtime.signup.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment saveComment(Long postId, CommentRequest request, Member member) {
        Comment parentComment = null;
        Post post;

        if (request.getParentCommentId() != null) {
            parentComment = findComment(request.getParentCommentId());

            // 대댓글에 댓글을 달 수 없도록 방지
            if (parentComment.getParentComment() != null) {
                throw new IllegalArgumentException("Cannot add a comment to a reply.");
            }

            // 여기 추가: 대댓글인 경우에는 parentComment에서 post 가져옴
            post = parentComment.getPost();
        } else {
            // 일반 댓글일 경우에는 postId로 post 조회
            post = findPost(postId);
        }

        return commentRepository.save(new Comment(request.getBody(), post, member, parentComment));
    }


    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    public Page<Comment> findCommentsByPostId(Long postId, Pageable pageable) {
        // 최상위 댓글만 조회
        return commentRepository.findByPostIdAndParentCommentIsNull(postId, pageable);
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentRequest request) {
        Comment comment = findComment(commentId);
        return comment.updateBody(request.getBody());
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
}

package com.est.runtime.comment.dto;

import com.est.runtime.comment.Comment;
import com.est.runtime.post.Post;
import com.est.runtime.post.dto.PostResponse;
import com.est.runtime.signup.dto.AuthorDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CommentResponse {
    private long commentId;
    private long postId;
    private String body;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private PostResponse post;
    private AuthorDTO author;
    private List<CommentResponse> replies;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.body = comment.getBody();
        this.createdAt = comment.getCreatedAt();

        Post postEntity = comment.getPost();
        this.postId = postEntity.getId();
        this.post = new PostResponse(postEntity);
        this.author = new AuthorDTO(comment.getMember());

        if (comment.getParentComment() == null) { // 최상위 댓글인 경우에만 replies를 초기화
            this.replies = comment.getChildComments().stream()
                    .map(CommentResponse::new)
                    .collect(Collectors.toList());
        }

    }
}

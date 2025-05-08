package com.est.runtime.comment;

import com.est.runtime.comment.dto.CommentRequest;
import com.est.runtime.comment.dto.CommentResponse;
import com.est.runtime.post.Post;
import com.est.runtime.post.PostService;
import com.est.runtime.signup.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/api/post/{postId}/comments")
    public ResponseEntity<CommentResponse> saveComment(@PathVariable("postId") Long postId,
                                                       @RequestBody CommentRequest request,
                                                       @AuthenticationPrincipal Member member) throws IOException {
        Comment comment = commentService.saveComment(postId, request, member);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommentResponse(comment));
    }

    @PostMapping("/api/comments/{parentCommentId}/replies")
    public ResponseEntity<CommentResponse> saveReply(@PathVariable("parentCommentId") Long parentCommentId,
                                                     @RequestBody CommentRequest request,
                                                     @AuthenticationPrincipal Member member) throws IOException {
        // CommentRequest에 parentCommentId를 설정하여 서비스 계층으로 전달
        request.setParentCommentId(parentCommentId);
        Comment reply = commentService.saveComment(null, request, member); // postId는 서비스 계층에서 찾음
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommentResponse(reply));
    }



    @GetMapping("/api/comments/{commentId}")
    public ResponseEntity<CommentResponse> findComment(@PathVariable("commentId") Long commentId) {
        Comment comment = commentService.findComment(commentId);
        return ResponseEntity.ok(new CommentResponse(comment));
    }

    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId,
                                                         @RequestBody CommentRequest request) {
        Comment comment = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(new CommentResponse(comment));
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<Page<CommentResponse>> findCommentsByPostId(@PathVariable Long articleId, Pageable pageable) {
        // 댓글을 페이지 단위로 조회
        Page<Comment> comments = commentService.findCommentsByPostId(articleId, pageable);

        // Page<Comment>를 Page<CommentResponse>로 변환하여 반환
        Page<CommentResponse> commentResponses = comments.map(CommentResponse::new);

        return ResponseEntity.ok(commentResponses);  // 페이지 단위로 댓글 반환
    }
}

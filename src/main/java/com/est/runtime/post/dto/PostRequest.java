package com.est.runtime.post.dto;

import com.est.runtime.board.Board;
import com.est.runtime.post.Post;
import com.est.runtime.signup.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private Long boardId; // 게시판 ID 추가

    public PostRequest(String title, String content, Long boardId) {
        this.title = title;
        this.content = content;
        this.boardId = boardId;
    }

    public Post toEntity(Member member, Board board) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .board(board) // Board 추가
                .build();
    }
}

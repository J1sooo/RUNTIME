package com.est.runtime.post.dto;

import com.est.runtime.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;

    public PostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity() {
        if (this.content == null || this.content.trim().isEmpty()) {
            this.content = "기본 내용";  // content가 비어있으면 기본값 설정
        }
        return new Post(this.title, this.content);
    }
}

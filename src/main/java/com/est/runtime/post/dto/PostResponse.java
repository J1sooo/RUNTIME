package com.est.runtime.post.dto;

import com.est.runtime.post.Post;
import com.est.runtime.post.img.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String title;
    private String content;
    private List<String> imgUrls;
    private LocalDateTime createdAt;

    public PostResponse(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imgUrls = post.getImages().stream()
                .map(Image::getImgUrl)
                .toList();
        this.createdAt = post.getCreatedAt();

    }
}


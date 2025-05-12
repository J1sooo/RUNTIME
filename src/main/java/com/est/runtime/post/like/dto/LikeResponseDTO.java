package com.est.runtime.post.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponseDTO {
    private boolean liked;
    private Long likeCount;
}
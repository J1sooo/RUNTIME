package com.est.runtime.post;

import com.est.runtime.post.dto.LikeResponseDTO;
import com.est.runtime.signup.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{id}/like")
    public ResponseEntity<LikeResponseDTO> toggleLike(
            @PathVariable Long id,
            @AuthenticationPrincipal Member loginUser) {

        LikeResponseDTO result = postLikeService.toggleLike(id, loginUser.getId());
        return ResponseEntity.ok(result);
    }

}


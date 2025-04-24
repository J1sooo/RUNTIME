package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/api/articles")
    public ResponseEntity<PostResponse> saveArticle(@RequestBody PostRequest request) {
        Post savedArticle = postService.saveArticle(request);

        // Article -> ArticleResponse 변환 후 리턴
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedArticle.toDto());
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<PostResponse>> findAllArticles() {
        List<Post> post = postService.findArticles();

        List<PostResponse> responseBody = post.stream()
                .map(PostResponse::new)
                .toList();

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        return postService.updatePost(id, request);
    }
}

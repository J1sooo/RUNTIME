package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import com.est.runtime.signup.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/api/post")
    public ResponseEntity<PostResponse> savePost(@RequestPart("post") PostRequest request,
                                                 @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                                 @AuthenticationPrincipal Member member) throws IOException {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Post post = postService.savePost(request, files, member);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(post.toDto());
    }

    @GetMapping("/api/post")
    public ResponseEntity<Page<PostResponse>> findAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postsPage = postService.findPosts(pageable);
        Page<PostResponse> response = postsPage.map(PostResponse::new);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/post/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @PutMapping("/api/post/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestPart("post") PostRequest request,
                                                   @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {
        Post post = postService.updatePost(id, request, files);
        return ResponseEntity.ok(post.toDto());
    }

    @GetMapping("/api/post/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        Post post = postService.findPost(id);
        return ResponseEntity.ok(post.toDto());
    }
}
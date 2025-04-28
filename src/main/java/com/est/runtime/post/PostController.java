package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<PostResponse> savePost(@RequestPart("post") PostRequest request,
                                                 @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {
        Post post = postService.savePost(request, files);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(post.toDto());
    }

//    //@GetMapping("/post")
//    public ResponseEntity<List<PostResponse>> findAllPosts() {
//        List<Post> post = postService.findPosts();
//
//        List<PostResponse> responseBody = post.stream()
//                .map(PostResponse::new)
//                .toList();
//
//        return ResponseEntity.ok(responseBody);
//    }

    @GetMapping("/post")
    public ResponseEntity<Page<PostResponse>> findAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Post> postsPage = postService.findPosts(PageRequest.of(page, size));
        Page<PostResponse> response = postsPage.map(post -> new PostResponse(post));  // Post 객체를 PostResponse로 변환

        return ResponseEntity.ok(response);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponse> findPostById(@PathVariable("id") Long id) {
        Post post = postService.findPost(id);
        return ResponseEntity.ok(post.toDto());
    }

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestPart("post") PostRequest request,
                                                   @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException{
        Post post = postService.updatePost(id, request, files);
        return ResponseEntity.ok(post.toDto());
    }
}

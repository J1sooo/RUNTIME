package com.est.runtime.post;

import com.est.runtime.board.Board;
import com.est.runtime.board.BoardRepository;
import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import com.est.runtime.signup.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    @PostMapping("/api/board/{boardId}/post")
    public ResponseEntity<PostResponse> savePost(@PathVariable Long boardId,
                                                 @RequestPart("post") PostRequest request,
                                                 @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                                 @AuthenticationPrincipal Member member) throws IOException {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Post post = postService.savePost(request, files, member, board);

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
    @GetMapping("/api/board/{boardId}/posts")
    public ResponseEntity<Page<PostResponse>> findPostsByBoard(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postsPage = postRepository.findByBoardAndHiddenFalse(board, pageable);
        Page<PostResponse> response = postsPage.map(PostResponse::new);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/search")
    public Page<PostResponse> searchPosts(@RequestParam(required = false) String title,
                                          @RequestParam(required = false) String nickname,
                                          @RequestParam (value = "boardId", required = false) Long boardId,
                                          @PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        if (title != null && !title.isEmpty()) {
            if (boardId != null) {
                return postService.searchPostsByTitle(title, boardId, pageable).map(PostResponse::new);
            } else {
                return postService.searchPostsByTitle(title, pageable).map(PostResponse::new);
            }
        }

        if (nickname != null && !nickname.isEmpty()) {
            if (boardId != null) {
                return postService.searchPostsByAuthorNickname(nickname, boardId, pageable).map(PostResponse::new);
            } else {
                return postService.searchPostsByAuthorNickname(nickname, pageable).map(PostResponse::new);
            }
        }
        return postService.findPosts(pageable).map(PostResponse::new);
    }

    @PatchMapping("/api/post/{id}/hide")
    public ResponseEntity<Map<String, Object>> toggleHidePost(@PathVariable Long id) {
        boolean isNowHidden = postService.toggleHidden(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("hidden", isNowHidden);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/post/{id}/unhide")
    public ResponseEntity<Void> unhidePost(@PathVariable Long id) {
        postService.unhidePost(id);
        return ResponseEntity.ok().build();
    }
}
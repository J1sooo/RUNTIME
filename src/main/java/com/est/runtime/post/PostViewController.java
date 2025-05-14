package com.est.runtime.post;

import com.est.runtime.comment.Comment;
import com.est.runtime.comment.CommentService;
import com.est.runtime.comment.dto.CommentResponse;
import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import com.est.runtime.signup.entity.Member;
import com.est.runtime.signup.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostViewController {
    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping("/post/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new PostRequest());
        return "newPost";
    }

    @GetMapping("/post")
    public String showPostList(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam Long board,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postService.findPostsByBoardId(board, pageable);
        Page<PostResponse> postResponsePage = postPage.map(PostResponse::new);

        model.addAttribute("posts", postResponsePage.getContent());
        model.addAttribute("currentPage", postPage.getNumber());
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("boardId", board);

        if (board == 4L) {
            return "crew";
        }

        return "postList";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable(name = "id") Long id, @RequestParam(defaultValue = "0") int page, Model model) {
        Post post = postService.findPost(id);
        PostResponse postResponse = new PostResponse(post);

        boolean hasImages = !post.getImages().isEmpty();
        boolean isAdmin = false;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Member mem) {
            if (mem.isAdmin()) {
                isAdmin = true;
            }
        }
        boolean isOwner = (post.getMember().getId() == memberService.isLoggedIn().getId());

        Pageable pageable = PageRequest.of(page, 10); // 10개씩 페이징
        Page<Comment> comments = commentService.findCommentsByPostId(id, pageable);
        Page<CommentResponse> commentResponses = comments.map(CommentResponse::new);

        model.addAttribute("board", post.getBoard());

        model.addAttribute("post", postResponse);
        model.addAttribute("hasImages", hasImages);
        model.addAttribute("isowner", isOwner);
        model.addAttribute("isadmin", isAdmin);
        model.addAttribute("comments", commentResponses);
        model.addAttribute("loggedInUserId", memberService.isLoggedIn().getId());

        return "postView";
    }

    @GetMapping("/crew")
    public String showCrewPage(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "5", name = "size") Integer size,
            Model model
    ) {

        if (size == null) {
            size = 5;
        }

        Long boardId = 3L;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postService.findPostsByBoardId(boardId, pageable);
        Page<PostResponse> postResponsePage = postPage.map(PostResponse::new);

        model.addAttribute("posts", postResponsePage.getContent());
        model.addAttribute("currentPage", postPage.getNumber());
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("boardId", boardId);

        return "crew";
    }

    @GetMapping("/posts/search-view")
    public String searchPostsView(@RequestParam String keyword,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam Long board,
                                  Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostResponse> posts;

        if (board != null) {
            // 특정 게시판 검색
            posts = postService.searchPostsByTitle(keyword, board, pageable).map(PostResponse::new);
        } else {
            // 전체 게시판 검색
            posts = postService.searchPostsByTitle(keyword, pageable).map(PostResponse::new);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("boardId", board);

        return "postList"; // templates/post/search.html 로 연결됨
    }
}
package com.est.runtime.post;

import com.est.runtime.comment.Comment;
import com.est.runtime.comment.CommentService;
import com.est.runtime.comment.dto.CommentResponse;
import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import com.est.runtime.signup.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        model.addAttribute("post", new PostRequest()); // "post"라는 이름으로 PostRequest 객체를 담아서 전달
        return "newPost";
    }

    @GetMapping("/post")
    public String showPostList(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postService.findPosts(pageable);
        Page<PostResponse> postResponsePage = postPage.map(PostResponse::new);

        model.addAttribute("posts", postResponsePage.getContent());
        model.addAttribute("currentPage", postPage.getNumber());
        model.addAttribute("totalPages", postPage.getTotalPages());

        return "postList";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable(name = "id") Long id, @RequestParam(defaultValue = "0") int page, Model model) {
        Post post = postService.findPost(id);
        PostResponse postResponse = new PostResponse(post);

        boolean hasImages = !post.getImages().isEmpty();
        boolean isOwner = (post.getMember().getId() == memberService.isLoggedIn().getId());

        Pageable pageable = PageRequest.of(page, 10); // 10개씩 페이징
        Page<Comment> comments = commentService.findCommentsByPostId(id, pageable);
        Page<CommentResponse> commentResponses = comments.map(CommentResponse::new);

        model.addAttribute("post", postResponse);
        model.addAttribute("hasImages", hasImages);
        model.addAttribute("isowner", isOwner);
        model.addAttribute("comments", commentResponses);
        model.addAttribute("loggedInUserId", memberService.isLoggedIn().getId());

        return "postView";
    }
}

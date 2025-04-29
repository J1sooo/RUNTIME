package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostViewController {
    private final PostService postService;

    @GetMapping("/articles/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new PostRequest());
        return "newArticle"; // templates/newArticle.html
    }

    @GetMapping("/api/post")
    public String showPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postService.findPosts(pageable);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", postPage.getNumber());
        model.addAttribute("totalPages", postPage.getTotalPages());

        return "articleList";
    }

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Optional<Post> postOptional = postService.getPostWithImages(id);

        if (postOptional.isEmpty()) {
            return "error/404"; // 게시글이 없으면 404 에러 페이지로 리다이렉트
        }

        Post post = postOptional.get();
        boolean hasImages = !post.getImages().isEmpty();

        model.addAttribute("post", post);
        model.addAttribute("hasImages", hasImages);

        return "postView";
    }
}

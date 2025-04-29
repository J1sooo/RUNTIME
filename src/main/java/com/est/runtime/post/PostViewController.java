package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @PostMapping("/api/post")
    public String createPost(@ModelAttribute PostRequest request,
                             @RequestParam List<MultipartFile> files) throws IOException {
        // 내용이 비어있을 경우 기본값을 설정
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            request.setContent("기본 내용");
        }

        // 게시글 생성 로직 (파일을 함께 저장)
        postService.savePost(request, files);  // 두 개의 인수를 전달

        // 이미지가 포함된 본문을 처리하기 위해 템플릿으로 전달
        return "redirect:/api/post";  // 리다이렉트하여 다른 화면으로 이동
    }


    @GetMapping("/api/post")
    public String showPostList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<Post> postPage = postService.findPosts(PageRequest.of(page, size));

        model.addAttribute("posts", postPage.getContent()); // 실제 게시글 리스트
        model.addAttribute("currentPage", postPage.getNumber());
        model.addAttribute("totalPages", postPage.getTotalPages());

        return "articleList"; // templates/articleList.html
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

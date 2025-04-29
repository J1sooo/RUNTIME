package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import com.est.runtime.s3.ImgUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImgUploadService imgUploadService;

    public Page<Post> findPosts(Pageable pageable) {
        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return postRepository.findAll(sortedByCreatedAtDesc);
    }

    @Transactional
    public PostResponse savePost(PostRequest request, List<MultipartFile> files) throws IOException {
        // 게시글 생성
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        // 파일 업로드 처리
        if (files != null && !files.isEmpty()) {
            imgUploadService.uploadFiles(files, post);  // 이미지 처리
        }

        // 게시글 저장
        post = postRepository.save(post);

        // PostResponse 반환 (title과 content만 포함)
        return new PostResponse(post.getTitle(), post.getContent());
    }

    public Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        imgUploadService.deleteFile(post.getImages());
        postRepository.deleteById(id);
    }

    @Transactional
    public Post updatePost(Long id, PostRequest request, List<MultipartFile> files) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (files != null && !files.isEmpty()) {
            imgUploadService.deleteFile(post.getImages());
            post.getImages().clear();

            imgUploadService.uploadFiles(files, post);
        }
        post.update(request.getTitle(), request.getContent());
        return post;
    }

}

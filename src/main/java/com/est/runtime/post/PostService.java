package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.img.Image;
import com.est.runtime.s3.ImgUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImgUploadService imgUploadService;

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public Post savePost(PostRequest request, List<MultipartFile> files) throws IOException {
        Post post = request.toEntity();
        if (files != null && !files.isEmpty()) {
            imgUploadService.uploadFiles(files, post);
        }
        return postRepository.save(post);
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

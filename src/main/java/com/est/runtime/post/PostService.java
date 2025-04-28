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
            List<Image> images = imgUploadService.uploadFiles(files, post);
            for (Image img : images) {
                post.addImg(img);
            }
        }
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        for (Image img : post.getImages()) {
            String fileName = img.getFileName();
            imgUploadService.deleteFile(fileName);
        }
        postRepository.deleteById(id);
    }

    @Transactional
    public Post updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.update(request.getTitle(), request.getContent());
        return post;
    }
}

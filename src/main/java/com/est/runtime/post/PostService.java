package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import com.est.runtime.post.dto.PostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public Post savePost(PostRequest request) {
        return postRepository.save(request.toEntity());
    }

    public void deletePost(Long id) {
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

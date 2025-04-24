package com.est.runtime.post;

import com.est.runtime.post.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findArticles(){
        return postRepository.findAll();
    }

    public Post saveArticle(PostRequest request) {
        return postRepository.save(request.toEntity());
    }
}

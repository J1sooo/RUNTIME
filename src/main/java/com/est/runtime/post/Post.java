package com.est.runtime.post;

import com.est.runtime.post.dto.PostResponse;
import com.est.runtime.post.img.Image;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Long likes = 0L;
    private Boolean hidden = true;

    @OneToMany (mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostResponse toDto() {
        return new PostResponse(this);
    }

    public void addImg(Image image) {
        this.images.add(image);
        image.setPost(this);
    }
}

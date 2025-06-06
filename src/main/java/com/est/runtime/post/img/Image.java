package com.est.runtime.post.img;

import com.est.runtime.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fileName;
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(String fileName, String imgUrl, Post post) {
        this.fileName = fileName;
        this.imgUrl = imgUrl;
        this.post = post;
    }
}

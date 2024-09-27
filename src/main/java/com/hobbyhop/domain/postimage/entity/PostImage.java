package com.hobbyhop.domain.postimage.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage extends BaseEntity {
    @Id
    private String uuid;
    private String fileName;

    @Length(max = 500)
    private String savedFileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public void changePost(Post post) {
        this.post = post;
    }
}

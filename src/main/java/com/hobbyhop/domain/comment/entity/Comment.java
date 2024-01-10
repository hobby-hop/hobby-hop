package com.hobbyhop.domain.comment.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String content;

    //@ManyToOne
    //User user;

    @ManyToOne
    Post post;

    public void changeContent(String content) {
        this.content = content;
    }
}

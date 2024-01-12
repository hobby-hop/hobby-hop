package com.hobbyhop.domain.postlike.entity;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike{

    @EmbeddedId
    private PostLikeKey postLikeKey;

    @Column(nullable = false)
    private Boolean isLiked;

    public static PostLike PostLikeBuilder(User user, Post post) {

        return PostLike.builder()
                .postLikeKey(PostLikeKey.builder()
                        .user(user)
                        .post(post)
                        .build())
                .isLiked(false)
                .build();
    }

    public Boolean updateLike() {
        this.isLiked = !isLiked;
        return this.isLiked;
    }
}
package com.hobbyhop.domain.postuser.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postuser.pk.PostUserPK;
import com.hobbyhop.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUser extends BaseEntity {
    @EmbeddedId
    private PostUserPK postUserPK;

    @Column(nullable = false)
    private Boolean isLiked;

    public static PostUser buildPostUser(User user, Post post) {
        return PostUser.builder()
                .postUserPK(PostUserPK.builder()
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
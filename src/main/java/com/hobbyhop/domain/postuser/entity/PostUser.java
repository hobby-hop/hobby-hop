package com.hobbyhop.domain.postuser.entity;

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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post_user SET deleted_at = NOW() where post_id=? and user_id=?")
@SQLRestriction("deleted_at is NULL")
public class PostUser {
    @EmbeddedId
    private PostUserPK postUserPK;

    @Column(nullable = false)
    private Boolean isLiked;

    @Column
    private Timestamp deletedAt;

    public static PostUser PostUserBuilder(User user, Post post) {
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
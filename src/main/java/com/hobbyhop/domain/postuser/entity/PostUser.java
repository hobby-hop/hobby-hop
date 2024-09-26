package com.hobbyhop.domain.postuser.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postuser.pk.PostUserPK;
import com.hobbyhop.domain.user.entity.User;
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
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUser extends BaseEntity {
    @EmbeddedId
    private PostUserPK postUserPK;

    public static PostUser buildPostUser(User user, Post post) {
        return PostUser.builder()
                .postUserPK(PostUserPK.builder()
                        .user(user)
                        .post(post)
                        .build())
                .build();
    }
}
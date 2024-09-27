package com.hobbyhop.domain.postuser.repository.custom;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postuser.entity.PostUser;
import com.hobbyhop.domain.user.entity.User;

import java.util.Optional;

public interface PostUserRepositoryCustom {
    Optional<PostUser> findPostUser(Long userId, Long postId);
}

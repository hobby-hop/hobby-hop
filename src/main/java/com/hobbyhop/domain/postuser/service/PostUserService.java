package com.hobbyhop.domain.postuser.service;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.user.entity.User;

public interface PostUserService {
    void postUser(User user, Post post);
}

package com.hobbyhop.domain.postuser.service;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postuser.entity.PostUser;
import com.hobbyhop.domain.user.entity.User;

public interface PostUserService {
    Long makePostUser(User user, Post post);
    PostUser findPostUser(User user, Post post);
}

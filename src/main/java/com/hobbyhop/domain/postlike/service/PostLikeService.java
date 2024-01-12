package com.hobbyhop.domain.postlike.service;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postlike.entity.PostLike;
import com.hobbyhop.domain.user.entity.User;

public interface PostLikeService {

    void postLike(User user, Post post);
}

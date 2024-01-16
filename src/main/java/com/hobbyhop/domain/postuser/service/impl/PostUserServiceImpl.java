package com.hobbyhop.domain.postuser.service.impl;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postuser.entity.PostUser;
import com.hobbyhop.domain.postuser.repository.PostUserRepository;
import com.hobbyhop.domain.postuser.service.PostUserService;
import com.hobbyhop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostUserServiceImpl implements PostUserService {

    private final PostUserRepository postUserRepository;

    public void postLike(User user, Post post) {

        PostUser postUser = postUserRepository.findByPostLikeKey_UserAndPostLikeKey_Post(user, post)
                .orElseGet(() -> savePostLike(user, post));

        Boolean update = postUser.updateLike();
        post.updateLikeCnt(update);
    }

    public PostUser savePostLike(User user, Post post) {

        return postUserRepository.save(PostUser.PostLikeBuilder(user, post));
    }
}

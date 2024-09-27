package com.hobbyhop.domain.postuser.service.impl;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postuser.entity.PostUser;
import com.hobbyhop.domain.postuser.repository.PostUserRepository;
import com.hobbyhop.domain.postuser.service.PostUserService;
import com.hobbyhop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostUserServiceImpl implements PostUserService {
    private final PostUserRepository postUserRepository;

    @Override
    public Long togglePostUser(User user, Post post) {
        Optional<PostUser> optionalPostUser = postUserRepository.findPostUser(user.getId(), post.getId());

        if (optionalPostUser.isPresent()) {
            postUserRepository.delete(optionalPostUser.get());
            post.subLikeCnt();
        } else {
            postUserRepository.save(PostUser.buildPostUser(user, post));
            post.addLikeCnt();
        }

        return post.getLikeCnt();
    }

    @Override
    public PostUser findPostUser(User user, Post post) {
        return postUserRepository.findPostUser(user.getId(), post.getId()).orElse(null);
    }
}
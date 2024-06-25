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

    public void postUser(User user, Post post) {
        PostUser postUser = postUserRepository.findByPostUserPK_UserAndPostUserPK_Post(user, post)
                .orElseGet(() -> savePostUser(user, post));

        Boolean update = postUser.updateLike();
        post.updateLikeCnt(update);
    }

    public PostUser savePostUser(User user, Post post) {
        return postUserRepository.save(PostUser.PostUserBuilder(user, post));
    }
}

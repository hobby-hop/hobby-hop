package com.hobbyhop.domain.postlike.service.impl;

import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postlike.entity.PostLike;
import com.hobbyhop.domain.postlike.repository.PostLikeRepository;
import com.hobbyhop.domain.postlike.service.PostLikeService;
import com.hobbyhop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLIkeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public void postLike(User user, Post post) {

        PostLike postLike = postLikeRepository.findByPostLikeKey_UserAndPostLikeKey_Post(user, post)
                .orElseGet(() -> savePostLike(user, post));

        Boolean update = postLike.updateLike();
        post.updateLikeCnt(update);
    }

    public PostLike savePostLike(User user, Post post) {

        PostLike postLike = PostLike.PostLikeBuilder(user, post);

        return postLikeRepository.save(postLike);
    }
}

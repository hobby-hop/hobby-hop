package com.hobbyhop.domain.postlike.repository;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postlike.entity.PostLike;
import com.hobbyhop.domain.postlike.entity.PostLikeKey;
import com.hobbyhop.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeKey> {

    Optional<PostLike> findByPostLikeKey_UserAndPostLikeKey_Post(User user, Post post);
}

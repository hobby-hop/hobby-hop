package com.hobbyhop.domain.post.repository;

import com.hobbyhop.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}

package com.hobbyhop.domain.postimage.repository;

import com.hobbyhop.domain.postimage.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}

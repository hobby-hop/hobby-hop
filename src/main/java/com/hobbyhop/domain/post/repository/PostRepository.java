package com.hobbyhop.domain.post.repository;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.custom.PostRepositoryCustom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Post p where p.club.id = :clubId and p.id = :postId")
    Optional<Post> findByIdWithOptimisticLock(@Param("clubId")Long clubId, @Param("postId")Long postId);

    Optional<Post> findByIdAndClub_Id(Long id, Long clubId);
}

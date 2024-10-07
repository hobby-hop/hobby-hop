package com.hobbyhop.domain.comment.repository;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.custom.CommentRepositoryCustom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Lock(LockModeType.OPTIMISTIC)
    @Query("select c from Comment c where c.id = :commentId")

    Optional<Comment> findByIdWithOptimisticLock(@Param("commentId") Long commentId);
}

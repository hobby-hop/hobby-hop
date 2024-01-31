package com.hobbyhop.domain.commentuser.repository;

import com.hobbyhop.domain.commentuser.entity.CommentUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentUserRepository extends JpaRepository<CommentUser, Long> {

    @Query(value = "SELECT * FROM comment_user WHERE comment_id = :commentId and user_id = :userId", nativeQuery = true)
    Optional<CommentUser> findCommentUserByIds(@Param("commentId") Long commentId, @Param("userId") Long userId);
}

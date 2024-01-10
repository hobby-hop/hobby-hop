package com.hobbyhop.domain.comment.repository;

import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.custom.CommentRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    //@Query("select Comment.content, User.username, Comment.createdAt from Comment left join User.id where Comment.postId = postId")
    Optional<List<CommentResponseDTO>> findByPostId(Long postId);
}

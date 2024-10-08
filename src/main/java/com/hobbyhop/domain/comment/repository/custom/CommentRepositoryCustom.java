package com.hobbyhop.domain.comment.repository.custom;

import com.hobbyhop.domain.comment.dto.CommentPageRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface CommentRepositoryCustom {
    Optional<Comment> findById(Long clubId, Long postId, Long commentId);

    List<CommentResponseDTO> findAllByPostId(Long postId, Long userId);

    void deleteList(List<Comment> list);
}

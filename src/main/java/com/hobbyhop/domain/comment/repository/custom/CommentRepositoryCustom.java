package com.hobbyhop.domain.comment.repository.custom;

import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.global.request.PageRequestDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CommentRepositoryCustom {
    Optional<Comment> findById(Long clubId, Long postId, Long commentId);
    Page<CommentResponseDTO> findAllByPostId(PageRequestDTO pageRequestDTO, Long postId);
    void deleteList(List<Comment> list);
}

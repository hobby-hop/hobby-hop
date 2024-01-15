package com.hobbyhop.domain.comment.repository.custom;

import com.hobbyhop.domain.comment.dto.CommentListResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.global.request.SortStandardRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    CommentListResponseDTO findAllByPostId(Pageable pageable, SortStandardRequest standard, Long postId);
}

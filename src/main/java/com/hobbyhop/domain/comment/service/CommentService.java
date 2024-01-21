package com.hobbyhop.domain.comment.service;

import com.hobbyhop.domain.comment.dto.CommentListResponseDTO;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.request.SortStandardRequest;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDTO postComment(CommentRequestDTO request, Long clubId, Long postId, User user);

    CommentResponseDTO postComment(CommentRequestDTO request, Long clubId, Long postId, Long commentId, User user);

    void patchComment(CommentRequestDTO requestDto, Long clubId, Long postId, Long commentId, User user);

    void deleteComment(Long clubId, Long postId, Long commentId, User user);

    CommentListResponseDTO getComments(Pageable pageable, SortStandardRequest standard, Long postId);

    void likeComment(Long clubId, Long postId, Long commentId, User user);
}

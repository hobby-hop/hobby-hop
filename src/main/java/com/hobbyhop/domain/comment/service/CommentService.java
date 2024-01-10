package com.hobbyhop.domain.comment.service;

import com.hobbyhop.domain.comment.dto.CommentListResponseDTO;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;

public interface CommentService {

    CommentResponseDTO postComment(CommentRequestDTO request, Long postId);
    void patchComment(CommentRequestDTO requestDto, Long commentId);
    void deleteComment(Long commentId);
    CommentListResponseDTO getComments(Long postId);
}

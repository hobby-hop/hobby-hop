package com.hobbyhop.domain.comment.service;

import com.hobbyhop.domain.comment.dto.CommentPageRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.response.PageResponseDTO;

import java.util.List;

public interface CommentService {
    CommentResponseDTO writeComment(CommentRequestDTO request, Long clubId, Long postId, User user);

    CommentResponseDTO writeReply(CommentRequestDTO request, Long clubId, Long postId, Long commentId, User user);

    CommentResponseDTO editComment(CommentRequestDTO requestDto, Long clubId, Long postId, Long commentId, User user);

    void deleteComment(Long clubId, Long postId, Long commentId, User user);

    List<CommentResponseDTO> getComments(Long clubId, Long postId, User user);

    Comment findById(Long clubId, Long postId, Long commentId);

    Long likeComment(Long clubId, Long postId, Long commentId, User user);
}

package com.hobbyhop.domain.comment.service;

import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.request.PageRequestDTO;
import com.hobbyhop.global.response.PageResponseDTO;

public interface CommentService {

    CommentResponseDTO postComment(CommentRequestDTO request, Long clubId, Long postId, User user);

    CommentResponseDTO postComment(CommentRequestDTO request, Long clubId, Long postId, Long commentId, User user);

    void patchComment(CommentRequestDTO requestDto, Long clubId, Long postId, Long commentId, User user);

    void deleteComment(Long clubId, Long postId, Long commentId, User user);

    PageResponseDTO<CommentResponseDTO> getComments(PageRequestDTO pageRequestDTO, Long postId);

    void likeComment(Long clubId, Long postId, Long commentId, User user);
}

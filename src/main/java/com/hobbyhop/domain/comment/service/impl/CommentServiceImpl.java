package com.hobbyhop.domain.comment.service.impl;

import com.hobbyhop.domain.comment.dto.*;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.CommentRepository;
import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.global.exception.common.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDTO postComment(CommentRequestDTO request, Long postId) {
        // Post를 불러와서 저장
        // Post post = PostRepository.findById(postId).ElseOr~;

        Comment comment = Comment.builder()
                .content(request.getContent())
                //post 넣기
                //user 넣기
                .build();
        commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }



    @Override
    @Transactional
    public void patchComment(CommentRequestDTO requestDto, Long commentId) {
        Comment comment = findById(commentId);
        comment.changeContent(requestDto.getContent());
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = findById(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public CommentListResponseDTO getComments(CommentPageable pageable, Long postId) {
        //post 조회
        List<CommentResponseDTO> commentList = commentRepository.findByPostId(postId).orElseThrow();

        return CommentListResponseDTO.builder()
                .page(pageable.getPage())
                .totalCount((int)(Math.ceil(commentList.size()/pageable.getSize())))
                .data(commentList)
                .build();
    }

    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(/*NOT_FOUND_COMMENT_EXCEPTION::new*/);
    }
}

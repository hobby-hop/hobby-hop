package com.hobbyhop.domain.comment.service.impl;

import com.hobbyhop.domain.comment.dto.CommentListResponseDTO;
import com.hobbyhop.domain.comment.dto.CommentRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.CommentRepository;
import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.domain.commentuser.service.CommentUserService;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.comment.CommentNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentUserService commentUserService;

    @Override
    public CommentResponseDTO postComment(CommentRequestDTO request, Long postId, User user) {
        Post post = postService.findPost(postId);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .build();
        commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .content(comment.getContent())
                .writer(comment.getUser().getUsername())
                .like(commentUserService.countLike(comment))
                .createdAt(comment.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public void patchComment(CommentRequestDTO requestDto, Long commentId, User user) {
        Comment comment = findById(commentId);
        comment.changeContent(requestDto.getContent());
    }

    @Override
    public void deleteComment(Long commentId, User user) {
        Comment comment = findById(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public CommentListResponseDTO getComments(Pageable pageable, Long postId) {
        return commentRepository.findAllByPostId(pageable, postId);
    }

    @Override
    public void likeComment(Long commentId, User user) {
        Comment comment = findById(commentId);
        commentUserService.modifyCommentUser(comment, user);
    }

    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }
}

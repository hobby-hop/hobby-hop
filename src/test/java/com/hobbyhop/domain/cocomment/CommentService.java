package com.hobbyhop.domain.cocomment;

import com.hobbyhop.comment.entity.Comment;
import com.hobbyhop.comment.exception.CommentErrorCode;
import com.hobbyhop.comment.exception.CommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse createComment(CommentRequest request, Long userId) {

        Comment comment = new Comment();
        comment.setCard(card);
        comment.setText(request.getText());
        comment.setAuthorId(userId);// 댓글 작성자 ID는 인증된 사용자의 ID로 설정

        Comment savedComment = commentRepository.save(comment);
        return convertEntityToResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByCardId(Long cardId) {
        return commentRepository.findByCardId(cardId).stream()
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthorId().equals(userId)) {
            throw new CommentException(CommentErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }

        comment.setText(request.getText());
        Comment updatedComment = commentRepository.save(comment);
        return convertEntityToResponse(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthorId().equals(userId)) {
            throw new CommentException(CommentErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }

        commentRepository.deleteById(commentId);
    }

    private CommentResponse convertEntityToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getAuthorId(),
                comment.getCreatedAt()
        );
    }
}

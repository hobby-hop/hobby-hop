package com.hobbyhop.domain.comment.facade;

import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.common.BusinessException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockCommentLikeFacade {
    private final CommentService commentService;

    public Long likeComment(Long clubId, Long postId, Long commentId, User user) throws InterruptedException {
        while(true){
            try {
                Long likeCnt = commentService.likeComment(clubId, postId, commentId, user);

                return likeCnt;
            } catch (OptimisticLockException e) {
                Thread.sleep(50);
            } catch (DataIntegrityViolationException e) {
                Thread.sleep(50);
            } catch (BusinessException e) {
                throw new RuntimeException("Unhandled exception occurred", e);
            }
        }
    }
}

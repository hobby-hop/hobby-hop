package com.hobbyhop.domain.comment.facade;

import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
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
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}

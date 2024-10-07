package com.hobbyhop.domain.comment.facade;

import com.hobbyhop.domain.comment.service.CommentService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import com.hobbyhop.global.exception.comment.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockCommentLikeFacade {
    private final CommentService commentService;

    public Long likeComment(Long clubId, Long postId, Long commentId, User user) throws InterruptedException {
        while (true) {
            try {
                Long likeCnt = commentService.likeComment(clubId, postId, commentId, user);

                return likeCnt;
            } catch (ObjectOptimisticLockingFailureException e) {
                Thread.sleep(50);
            } catch (DataIntegrityViolationException e) {
                Thread.sleep(50);
            } catch (CommentNotFoundException e) {
                throw new CommentNotFoundException();
            } catch (ClubMemberNotFoundException e) {
                throw new ClubMemberNotFoundException();
            }
        }
    }
}

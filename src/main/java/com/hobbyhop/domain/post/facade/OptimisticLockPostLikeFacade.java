package com.hobbyhop.domain.post.facade;

import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.common.BusinessException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockPostLikeFacade {
    private final PostService postService;

    public Long likePost(User user, Long clubId, Long postId) throws InterruptedException {
        while(true){
            try {
                Long likeCnt = postService.likePost(user, clubId, postId);

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

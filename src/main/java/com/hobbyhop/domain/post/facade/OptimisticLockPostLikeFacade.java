package com.hobbyhop.domain.post.facade;

import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class OptimisticLockPostLikeFacade {
    private final PostService postService;

    public Long likePost(User user, Long clubId, Long postId) throws InterruptedException {
        while(true){
            try {
                Long likeCnt = postService.likePost(user, clubId, postId);
                log.info("Successfully liked post. Like count: {}", likeCnt);
                return likeCnt;
            } catch (Exception e) {
                log.error(e.getMessage());
                Thread.sleep(50);
            }
        }
    }
}

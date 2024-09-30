package com.hobbyhop.domain.post.facade;

import com.hobbyhop.domain.post.service.PostService;
import com.hobbyhop.domain.user.entity.User;
import com.hobbyhop.global.exception.clubmember.ClubMemberNotFoundException;
import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.post.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
                return likeCnt;
            } catch (ObjectOptimisticLockingFailureException e) {
                Thread.sleep(50);
            } catch(DataIntegrityViolationException e) {
                Thread.sleep(50);
            } catch (PostNotFoundException e) {
                throw new PostNotFoundException();
            } catch (ClubMemberNotFoundException e) {
                throw new ClubMemberNotFoundException();
            }
        }
    }
}
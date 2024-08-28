package com.hobbyhop.domain.commentuser.service.impl;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.repository.CommentUserRepository;
import com.hobbyhop.domain.commentuser.service.CommentUserService;
import com.hobbyhop.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentUserServiceImpl implements CommentUserService {
    private final CommentUserRepository commentUserRepository;

    @Override
    @Transactional
    public void modifyCommentUser(Comment comment, User user) {
        CommentUser commentUser = commentUserRepository
                .findCommentUserByIds(comment.getId(), user.getId()).orElseGet(() -> saveCommentUser(comment, user));

        Boolean updated = commentUser.updateLike();
        comment.updateLikeCnt(updated);
    }

    private CommentUser saveCommentUser(Comment comment, User user) {
        return commentUserRepository.save(CommentUser.buildCommentUser(comment, user));
    }

}
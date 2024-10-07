package com.hobbyhop.domain.commentuser.service.impl;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.repository.CommentUserRepository;
import com.hobbyhop.domain.commentuser.service.CommentUserService;
import com.hobbyhop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentUserServiceImpl implements CommentUserService {
    private final CommentUserRepository commentUserRepository;

    @Override
    public Long toggleCommentUser(Comment comment, User user) {
        Optional<CommentUser> optionalCommentUser = commentUserRepository.findCommentUser(comment.getId(), user.getId());

        if(optionalCommentUser.isPresent()) {
            commentUserRepository.delete(optionalCommentUser.get());
            comment.subLike();
        } else {
            commentUserRepository.save(CommentUser.buildCommentUser(comment, user));
            comment.addLike();
        }

        return comment.getLikeCnt();
    }

    @Override
    public CommentUser findCommentUser(Long commentId, Long userId) {
        return commentUserRepository.findCommentUser(commentId, userId).orElse(null);
    }
}
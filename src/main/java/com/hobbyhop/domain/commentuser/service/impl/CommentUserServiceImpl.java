package com.hobbyhop.domain.commentuser.service.impl;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.repository.CommentUserRepository;
import com.hobbyhop.domain.commentuser.service.CommentUserService;
import com.hobbyhop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentUserServiceImpl implements CommentUserService {

    private final CommentUserRepository commentUserRepository;

    public void modifyCommentUser(Comment comment, User user) {
        CommentUser commentUser = commentUserRepository
                .findCommentUserByIds(comment.getId(), user.getId()).orElse(null);
        if(commentUser == null){
            saveCommentUser(comment, user);
        } else {
            deleteCommentUser(commentUser);
        }
    }

    public int countLike(Comment comment){
        return commentUserRepository.countLike(comment.getId());
    }

    private void saveCommentUser(Comment comment, User user) {
        CommentUser commentUser = CommentUser.buildCommentUser(comment, user);
        commentUserRepository.save(commentUser);
    }

    private void deleteCommentUser(CommentUser commentUser) {
        commentUserRepository.delete(commentUser);
    }
}
package com.hobbyhop.domain.commentuser.service;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.repository.CommentUserRepository;
import com.hobbyhop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentUserService {

    private final CommentUserRepository commentUserRepository;

    public void modifyCommentUser(Comment comment, User user) {
        CommentUser commentUser = commentUserRepository.findByCommentUserPK_CommentAndCommentUserPK_User(comment, user).orElse(null);
        if(commentUser == null){
            saveCommentUser(comment, user);
        } else {
            deleteCommentUser(commentUser);
        }
    }

    public int countLike(Comment comment){
        return commentUserRepository.countByCommentUserPK_Comment(comment);
    }

    private void saveCommentUser(Comment comment, User user) {
        CommentUser commentUser = CommentUser.buildCommentUser(comment, user);
        commentUserRepository.save(commentUser);
    }

    private void deleteCommentUser(CommentUser commentUser) {
        commentUserRepository.delete(commentUser);
    }
}
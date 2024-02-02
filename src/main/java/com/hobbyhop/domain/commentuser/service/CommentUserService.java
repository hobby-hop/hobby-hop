package com.hobbyhop.domain.commentuser.service;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.user.entity.User;

public interface CommentUserService {
    void modifyCommentUser(Comment comment, User user);
    int countLike(Comment comment);
}

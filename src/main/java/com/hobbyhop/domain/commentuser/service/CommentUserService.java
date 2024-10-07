package com.hobbyhop.domain.commentuser.service;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.user.entity.User;

public interface CommentUserService {
    Long toggleCommentUser(Comment comment, User user);
    CommentUser findCommentUser(Long commentId, Long userId);
}

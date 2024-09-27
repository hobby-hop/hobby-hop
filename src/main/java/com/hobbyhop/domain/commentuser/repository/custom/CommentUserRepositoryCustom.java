package com.hobbyhop.domain.commentuser.repository.custom;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.user.entity.User;

import java.util.Optional;

public interface CommentUserRepositoryCustom {
    Optional<CommentUser> findCommentUser(Long commentId, Long userId);
}

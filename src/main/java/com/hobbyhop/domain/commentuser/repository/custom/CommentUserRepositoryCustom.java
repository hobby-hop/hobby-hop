package com.hobbyhop.domain.commentuser.repository.custom;

import com.hobbyhop.domain.commentuser.entity.CommentUser;
import java.util.Optional;

public interface CommentUserRepositoryCustom {
    int countLike(Long commentId);
}

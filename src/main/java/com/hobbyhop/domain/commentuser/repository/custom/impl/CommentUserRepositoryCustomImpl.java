package com.hobbyhop.domain.commentuser.repository.custom.impl;

import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.repository.custom.CommentUserRepositoryCustom;
import com.hobbyhop.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;

@RequiredArgsConstructor
public class CommentUserRepositoryCustomImpl implements CommentUserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<CommentUser> findCommentUser(Long commentId, Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(commentUser)
                .where(commentUser.commentUserPK.comment.id.eq(commentId)
                        .and(commentUser.commentUserPK.user.id.eq(userId)))
                .fetchOne());
    }
}

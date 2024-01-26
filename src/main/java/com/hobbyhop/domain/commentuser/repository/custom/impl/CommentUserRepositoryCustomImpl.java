package com.hobbyhop.domain.commentuser.repository.custom.impl;

import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;

import com.hobbyhop.domain.commentuser.entity.CommentUser;
import com.hobbyhop.domain.commentuser.repository.custom.CommentUserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentUserRepositoryCustomImpl implements CommentUserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int countLike(Long commentId) {
        return jpaQueryFactory
                .selectFrom(commentUser)
                .where(commentUser.commentUserPK.comment.id.eq(commentId))
                .fetch().size();
    }
}
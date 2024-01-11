package com.hobbyhop.domain.commentuser.repository.custom;

//import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentUserRepositoryCustomImpl implements CommentUserRepositoryCustom {

//    private final JPAQueryFactory jpaQueryFactory;
//
//    public Optional<CommentUser> findCommentUserByIds(Long commentId, Long userId) {
//        return jpaQueryFactory
//                .from(commentUser)
//                .where(commentUser.commentUserPK.comment.id.eq(commentId)
//                        .and(commentUser.commentUserPK.user.id.eq(userId)))
//                .fetchOne();
//    }
}
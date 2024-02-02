package com.hobbyhop.domain.category.repository.custom.impl;

import static com.hobbyhop.domain.category.entity.QCategory.category;
import static com.hobbyhop.domain.club.entity.QClub.club;
import static com.hobbyhop.domain.clubmember.entity.QClubMember.clubMember;
import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.postuser.entity.QPostUser.postUser;

import com.hobbyhop.domain.category.dto.CategoryElementVO;
import com.hobbyhop.domain.category.repository.custom.CategoryRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteAllElement(Long categoryId) {
        List<CategoryElementVO> ids = jpaQueryFactory
                .select(
                        Projections.constructor(
                                CategoryElementVO.class,
                                club.id,
                                post.id,
                                comment.id
                        )
                )
                .from(club)
                .join(post)
                .on(post.club.id.eq(club.id))
                .join(comment)
                .on(comment.post.id.eq(post.id))
                .where(club.category.id.eq(categoryId))
                .fetch();

        Long clubId = ids.get(0).getClubId();
        Set<Long> postIds = new HashSet<>();
        Set<Long> commentIds = new HashSet<>();

        ids.forEach((id) -> {
            postIds.add(id.getPostId());
        });
        ids.forEach((id) -> {
            commentIds.add(id.getCommentId());
        });

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        jpaQueryFactory.update(commentUser)
                .set(commentUser.deletedAt, now)
                .where(commentUser.commentUserPK.comment.id.in(commentIds))
                .execute();

        jpaQueryFactory.update(comment)
                .set(comment.deletedAt, now)
                .where(comment.id.in(commentIds))
                .execute();

        jpaQueryFactory.update(postUser)
                .set(postUser.deletedAt, now)
                .where(postUser.postUserPK.post.id.in(postIds))
                .execute();

        jpaQueryFactory.update(post)
                .set(post.deletedAt, now)
                .where(post.id.in(postIds))
                .execute();

        jpaQueryFactory.update(clubMember)
                .set(clubMember.deletedAt, now)
                .where(clubMember.clubMemberPK.club.id.eq(clubId))
                .execute();

        jpaQueryFactory.update(club)
                .set(club.deletedAt, now)
                .where(club.id.eq(clubId))
                .execute();

        jpaQueryFactory.update(category)
                .set(category.deletedAt, now)
                .where(category.id.eq(categoryId))
                .execute();
    }
}

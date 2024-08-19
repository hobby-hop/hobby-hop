package com.hobbyhop.domain.club.repository.custom.impl;

import static com.hobbyhop.domain.club.entity.QClub.club;
import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.clubmember.entity.QClubMember.clubMember;
import static com.hobbyhop.domain.postuser.entity.QPostUser.postUser;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.dsl.Expressions.numberTemplate;

import com.hobbyhop.domain.club.dto.ClubElementVO;
import com.hobbyhop.domain.club.dto.ClubPageRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.entity.Club;
import com.hobbyhop.domain.club.repository.custom.ClubRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ClubRepositoryCustomImpl extends QuerydslRepositorySupport implements ClubRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public ClubRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Club.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<ClubResponseDTO> findAll(ClubPageRequestDTO pageRequestDTO) {
        JPAQuery<ClubResponseDTO> query= jpaQueryFactory
                .select(
                        Projections.constructor(
                                ClubResponseDTO.class,
                                club.id,
                                club.title,
                                club.content,
                                club.category.id.as("categoryId"),
                                club.category.categoryName,
                                club.createdAt,
                                club.modifiedAt
                        ))
                .from(club)
                .where(contains(club.title, pageRequestDTO.getKeyword()),
                        eqCategory(pageRequestDTO.getCategoryId()));

        Pageable pageable = pageRequestDTO.getPageable(pageRequestDTO.getSortBy());
        List<ClubResponseDTO> content = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = jpaQueryFactory
                .select(club.count())
                .from(club)
                .where(contains(club.title, pageRequestDTO.getKeyword()),
                        eqCategory(pageRequestDTO.getCategoryId()))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression contains(StringPath target, String searchWord) {
        if (searchWord == null || searchWord.isBlank()) {
            return null;
        }
        searchWord = searchWord.trim();
        if (searchWord.length() == 1) {
            return target.containsIgnoreCase(searchWord);
        } else {
            final String formattedSearchWord = "\"" +  searchWord + "*" + "\"";
            return numberTemplate(Double.class, "function('match_against', {0}, {1})",
                    target, formattedSearchWord)
                    .gt(0);
        }
    }

    private BooleanExpression eqCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return club.category.id.eq(categoryId);
    }

    @Override
    public void deleteAllElement(Long clubId) {
        List<ClubElementVO> ids = jpaQueryFactory.select(post.id, comment.id)
                .from(post)
                .join(comment)
                .on(post.id.eq(comment.post.id))
                .where(post.club.id.eq(clubId))
                .transform(
                        groupBy(post.id).list(
                                Projections.constructor(
                                        ClubElementVO.class,
                                        post.id,
                                        Projections.list(
                                                comment.id
                                        ))
                        )
                );
        List<Long> postIds = new ArrayList<>();
        List<Long> commentIds = new ArrayList<>();

        ids.forEach((id) -> {
            postIds.add(id.getPostId());
        });
        ids.forEach((id) -> {
            commentIds.addAll(id.getCommentId());
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
    }
}

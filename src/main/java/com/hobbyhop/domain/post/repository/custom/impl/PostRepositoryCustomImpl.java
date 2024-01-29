package com.hobbyhop.domain.post.repository.custom.impl;

import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.postuser.entity.QPostUser.postUser;
import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.custom.PostRepositoryCustom;
import com.hobbyhop.global.request.PageRequestDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

public class PostRepositoryCustomImpl extends QuerydslRepositorySupport implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<PostResponseDTO> findAllByClubId(Pageable pageable, Long clubId, String keyword){
        JPAQuery<PostResponseDTO> query = queryFactory
                .select(
                        Projections.constructor(
                                PostResponseDTO.class,
                                post.club.id,
                                post.id,
                                user.username,
                                post.postTitle,
                                post.postContent,
                                post.originImageUrl,
                                post.savedImageUrl,
                                post.likeCnt,
                                post.createdAt,
                                post.modifiedAt
                        )
                )
                .from(post)
                .join(user).fetchJoin()
                .on(post.user.id.eq(user.id))
                .where(post.club.id.eq(clubId));

        if (keyword != null) {
            BooleanExpression titleContainsKeyword = post.postTitle.containsIgnoreCase(keyword);
            query.where(titleContainsKeyword.or(titleContainsKeyword));
        }


        List<PostResponseDTO> content = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Page<PostResponseDTO> findAllByClubIdAndKeyword(PageRequestDTO pageRequestDTO, Long clubId){
        List<PostResponseDTO> content = queryFactory
                .select(
                        Projections.constructor(
                                PostResponseDTO.class,
                                post.club.id,
                                post.id,
                                user.username,
                                post.postTitle,
                                post.postContent,
                                post.originImageUrl,
                                post.savedImageUrl,
                                post.likeCnt,
                                post.createdAt,
                                post.modifiedAt
                        )
                )
                .from(post)
                .join(user).fetchJoin()
                .on(post.user.id.eq(user.id))
                .where(post.postTitle.eq(pageRequestDTO.getKeyword()),
                        post.club.id.eq(clubId))
                .groupBy(post.id)
                .orderBy(post.createdAt.desc())
                .offset(pageRequestDTO.getPageable().getOffset())
                .limit(pageRequestDTO.getSize())
                .fetch();

        JPAQuery<Long> count = queryFactory.select(post.count()).from(post);

        return PageableExecutionUtils.getPage(content, pageRequestDTO.getPageable(), count::fetchOne);
    }

    @Override
    public void deleteAllElement(Long postId) {
        List<Long> ids = queryFactory
                .select(comment.id)
                .from(comment)
                .where(comment.post.id.eq(postId))
                .fetch().stream().distinct().toList();

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        queryFactory.update(commentUser)
                .set(commentUser.deletedAt, now)
                .where(commentUser.commentUserPK.comment.id.in(ids))
                .execute();

        queryFactory.update(comment)
                .set(comment.deletedAt, now)
                .where(comment.id.in(ids))
                .execute();

        queryFactory.update(postUser)
                .set(postUser.deletedAt, now)
                .where(postUser.postUserPK.post.id.eq(postId))
                .execute();

        queryFactory.update(post)
                .set(post.deletedAt, now)
                .where(post.id.eq(postId))
                .execute();
    }
}

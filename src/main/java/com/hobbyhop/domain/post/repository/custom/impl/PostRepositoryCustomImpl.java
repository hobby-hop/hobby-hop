package com.hobbyhop.domain.post.repository.custom.impl;

import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.postuser.entity.QPostUser.postUser;
import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.post.dto.PostPageRequestDTO;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.repository.custom.PostRepositoryCustom;
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

public class PostRepositoryCustomImpl extends QuerydslRepositorySupport implements PostRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        super(Post.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<PostPageResponseDTO> findAllByClubId(PostPageRequestDTO pageRequestDTO, Long clubId){
        JPAQuery<PostPageResponseDTO> query = queryFactory
                .select(
                        Projections.constructor(
                                PostPageResponseDTO.class,
                                post.club.id,
                                post.id,
                                user.username,
                                post.postTitle,
                                post.likeCnt,
                                post.createdAt,
                                post.modifiedAt
                        )
                )
                .from(post)
                .where(post.club.id.eq(clubId));

        if (pageRequestDTO.getKeyword() != null) {
            BooleanExpression titleContainsKeyword = post.postTitle.containsIgnoreCase(pageRequestDTO.getKeyword());
            query.where(titleContainsKeyword);
        }
        Pageable pageable = pageRequestDTO.getPageable(pageRequestDTO.getSortBy());

        List<PostPageResponseDTO> content = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(content, pageable, totalCount);
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

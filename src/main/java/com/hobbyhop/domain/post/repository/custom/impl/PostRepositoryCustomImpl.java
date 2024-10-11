package com.hobbyhop.domain.post.repository.custom.impl;

import static com.hobbyhop.domain.club.entity.QClub.club;
import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.postimage.entity.QPostImage.postImage;
import static com.hobbyhop.domain.postuser.entity.QPostUser.postUser;
import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.post.dto.PostPageRequestDTO;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.post.entity.QPost;
import com.hobbyhop.domain.post.repository.custom.PostRepositoryCustom;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostPageResponseDTO> findAllByClubId(PostPageRequestDTO pageRequestDTO, Long clubId) {
        Pageable pageable = pageRequestDTO.getPageable(pageRequestDTO.getSortBy());
        List<PostPageResponseDTO> content = jpaQueryFactory
                .select(
                        Projections.constructor(
                                PostPageResponseDTO.class,
                                post.club.id,
                                post.id,
                                post.title,
                                post.user.username,
                                post.likeCnt,
                                post.createdAt,
                                post.modifiedAt
                        )
                )
                .from(post)
                .where(post.club.id.eq(clubId),
                        eqKeyword(pageRequestDTO.getKeyword()))
                .limit(pageRequestDTO.getSize())
                .offset(pageable.getOffset())
                .orderBy(createOrderSpecifier(pageable))
                .fetch();

        long totalCount = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.club.id.eq(clubId),
                        eqKeyword(pageRequestDTO.getKeyword()))
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression eqKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return post.title.containsIgnoreCase(keyword);
    }

    private OrderSpecifier<?> createOrderSpecifier(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            return null;
        }

        Sort.Order order = pageable.getSort().iterator().next();
        PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());

        return new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(order.getProperty()));
    }

    @Override
    public Optional<Post> findByIdWithImages(Long clubId, Long postId) {
        return Optional.ofNullable(jpaQueryFactory.select(post)
                .from(post)
                .leftJoin(post.imageSet, postImage).fetchJoin()
                .join(post.user).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne());
    }

    @Override
    public void deleteAllElement(Long postId) {
        List<Long> ids = jpaQueryFactory
                .select(comment.id)
                .from(comment)
                .where(comment.post.id.eq(postId))
                .fetch().stream().distinct().toList();

        jpaQueryFactory.delete(commentUser)
                .where(commentUser.commentUserPK.comment.id.in(ids))
                .execute();

        jpaQueryFactory.delete(comment)
                .where(comment.id.in(ids))
                .execute();

        jpaQueryFactory.delete(postUser)
                .where(postUser.postUserPK.post.id.eq(postId))
                .execute();

        jpaQueryFactory.delete(postImage)
                .where(postImage.post.id.eq(postId))
                .execute();

        jpaQueryFactory.delete(post)
                .where(post.id.eq(postId))
                .execute();
    }
}

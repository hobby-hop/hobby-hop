package com.hobbyhop.domain.post.repository.custom.impl;

import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.postuser.entity.QPostUser.postUser;
import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.repository.custom.PostRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PostPageResponseDTO findAllByClubId(Pageable pageable, Long clubId){
        JPAQuery<PostResponseDTO> query = queryFactory
                .select(
                        Projections.constructor(
                                PostResponseDTO.class,
                                post.club.id,
                                post.id,
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
                .leftJoin(postUser)
                .on(post.id.eq(postUser.postUserPK.post.id))
                .join(user)
                .on(post.user.id.eq(user.id))
                .where(post.club.id.eq(clubId))
                .groupBy(post.id)
                .orderBy(post.createdAt.desc())
                .fetchAll();

        List<PostResponseDTO> postPageList = query.fetch();

        return PostPageResponseDTO.builder()
                .page(pageable.getPageNumber())
                .totalCount(query.fetchAll().stream().count())
                .data(postPageList)
                .build();
    }

    @Override
    public void deleteAllElement(Long postId) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        queryFactory.update(comment)
                .set(comment.deletedAt, now)
                .where(comment.post.id.eq(postId))
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

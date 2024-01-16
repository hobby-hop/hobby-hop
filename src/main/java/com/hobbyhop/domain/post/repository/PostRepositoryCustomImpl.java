package com.hobbyhop.domain.post.repository;

import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.postlike.entity.QPostLike.postLike;
import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PostPageResponseDTO findAllByClubId(Pageable pageable, Long clubId){
        QueryResults<PostResponseDTO> query = queryFactory
                .select(
                        Projections.constructor(
                                PostResponseDTO.class,
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
                .leftJoin(postLike)
                .on(post.id.eq(postLike.postLikeKey.post.id))
                .join(user)
                .on(post.user.id.eq(user.id))
                .where(post.club.id.eq(clubId))
                .groupBy(post.id)
                .orderBy(post.createdAt.desc())
                .fetchResults();

        List<PostResponseDTO> postPageList = query.getResults();

        return PostPageResponseDTO.builder()
                .page(pageable.getPageNumber())
                .totalCount(query.getTotal())
                .data(postPageList)
                .build();
    }
}

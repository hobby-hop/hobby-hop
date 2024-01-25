package com.hobbyhop.domain.post.repository.custom.impl;

import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.postuser.entity.QPostUser.postUser;
import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.post.dto.PostPageResponseDTO;
import com.hobbyhop.domain.post.dto.PostResponseDTO;
import com.hobbyhop.domain.post.repository.custom.PostRepositoryCustom;
import com.hobbyhop.global.request.PageRequestDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostResponseDTO> findAllByClubId(PageRequestDTO pageRequestDTO, Long clubId){
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
                .join(user)
                .on(post.user.id.eq(user.id))
                .where(post.club.id.eq(clubId))
                .groupBy(post.id)
                .orderBy(post.createdAt.desc())
                .fetch();

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());

        List<PostResponseDTO> paging = new ArrayList<>();

        for (int i = (pageable.getPageNumber() - 1) * pageable.getPageSize(); i < Long.valueOf(pageable.getOffset()).intValue() && i < content.size(); i++) {
            paging.add(content.get(i));
        }

        return new PageImpl<>(paging, pageable, content.size());
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

package com.hobbyhop.domain.club.repository.custom.impl;

import static com.hobbyhop.domain.club.entity.QClub.club;
import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.clubmember.entity.QClubMember.clubMember;
import static com.querydsl.core.group.GroupBy.groupBy;

import com.hobbyhop.domain.club.dto.ClubElementVO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.repository.custom.ClubRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ClubRepositoryCustomImpl implements ClubRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ClubResponseDTO> findAll(Pageable pageable, String keyword) {
        List<ClubResponseDTO> content = jpaQueryFactory
                .select(
                        Projections.constructor(
                                ClubResponseDTO.class,
                                club.id,
                                club.title,
                                club.content,
                                club.category.categoryName,
                                club.createdAt,
                                club.modifiedAt,
                                club.category.id.as("categoryId")))
                .from(club)
                .fetch();
        int count = content.size();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public void deleteAllElement(Long clubId){
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

        ids.forEach((id) -> { postIds.add(id.getPostId()); });
        ids.forEach((id) -> { commentIds.addAll(id.getCommentId()); });

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());

        jpaQueryFactory.update(comment)
                .set(comment.deletedAt, now)
                .where(comment.id.in(commentIds))
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

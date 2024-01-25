package com.hobbyhop.domain.comment.repository.custom.impl;

import com.hobbyhop.domain.comment.dto.CommentListResponseDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.dto.CommentVO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.custom.CommentRepositoryCustom;
import com.hobbyhop.global.request.SortStandardRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Comment> findById(Long clubId, Long postId, Long commentId){
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(comment)
                .join(post)
                .on(comment.post.id.eq(post.id))
                .where(comment.id.eq(commentId).and(post.id.eq(postId)).and(post.club.id.eq(clubId)))
                .fetchOne());
    }

    @Override
    public CommentListResponseDTO findAllByPostId(Pageable pageable, SortStandardRequest standard, Long postId) {
        List<CommentVO> query = jpaQueryFactory
                .select(
                        Projections.constructor(
                                CommentVO.class,
                                comment.content,
                                user.username,
                                commentUser.count().intValue(),
                                comment.createdAt,
                                comment.id,
                                comment.parent.id
                        )
                )
                .from(comment)
                .leftJoin(commentUser)
                .on(comment.id.eq(commentUser.commentUserPK.comment.id))
                .join(user)
                .on(comment.user.id.eq(user.id))
                .where(comment.post.id.eq(postId))
                .groupBy(comment.id)
                .orderBy(comment.id.asc())
                .fetch();

        List<CommentResponseDTO> content = VOtoDTO(query);

        sort(standard, content);

        List<CommentResponseDTO> paging = new ArrayList<>();

        for (int i = (pageable.getPageNumber() - 1) * pageable.getPageSize(); i < Long.valueOf(pageable.getOffset()).intValue() && i < content.size(); i++) {
            paging.add(content.get(i));
        }

        //Page<CommentResponseDTO> data = new PageImpl<>(paging, pageable, query.size());

        return CommentListResponseDTO.builder()
                .page(pageable.getPageNumber())
                .totalCount(content.size())
                .data(paging)
                .build();
    }

    private List<CommentResponseDTO> VOtoDTO(List<CommentVO> query) {
        List<CommentResponseDTO> content = new ArrayList<>();

        for(CommentVO c: query){
            if(c.getParent() == null){
                content.add(CommentResponseDTO.builder()
                        .content(c.getContent())
                        .writer(c.getWriter())
                        .like(c.getLike())
                        .createdAt(c.getCreatedAt())
                        .reply(addVOtoDTO(query, c.getId()))
                        .build());
            }
        }
        return content;
    }

    private List<CommentResponseDTO> addVOtoDTO(List<CommentVO> vo, Long id){
        List<CommentResponseDTO> dto = new ArrayList<>();
        for(CommentVO c: vo){
            if(Objects.equals(c.getParent(), id)){
                dto.add(CommentResponseDTO.builder()
                        .content(c.getContent())
                        .writer(c.getWriter())
                        .like(c.getLike())
                        .createdAt(c.getCreatedAt())
                        .reply(addVOtoDTO(vo, c.getId()))
                        .build());
            }
        }
        return dto;
    }

    private void sort(SortStandardRequest standard, List<CommentResponseDTO> content){
        for (CommentResponseDTO c : content) {
            if (c.getReply().size() > 3)
                sort(standard, c.getReply());
            if (c.getReply().size() < 2)
                return;
        }

        switch (standard.getSortStandard()) {
            case 1:
                if (standard.isDesc()) {
                    content.sort(Comparator.comparing(CommentResponseDTO::getLike));
                } else {
                    content.sort(Comparator.comparing(CommentResponseDTO::getLike).reversed());
                }
                break;
            case 2:
                if (standard.isDesc()) {
                    content.sort(Comparator.comparingInt(c -> c.getReply().size()));
                } else {
                    content.sort((c1, c2) -> Integer.compare(c2.getReply().size(), c1.getReply().size()));
                }
                break;
            default:
                if (standard.isDesc()) {
                    content.sort(Comparator.comparing(CommentResponseDTO::getCreatedAt));
                } else {
                    content.sort(Comparator.comparing(CommentResponseDTO::getCreatedAt).reversed());
                }
                break;
        }
    }

    @Override
    public void deleteList(List<Comment> deletelist) {
        List<Long> deleteId = new ArrayList<>();
        deletelist.forEach((d) -> {
            deleteId.add(d.getId());
        });

        Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
        jpaQueryFactory.update(comment).set(comment.deletedAt, ts)
                .where(comment.id.in(deleteId)).execute();


        jpaQueryFactory.update(commentUser).set(commentUser.deletedAt, ts)
                .where(commentUser.commentUserPK.comment.id.in(deleteId)).execute();
    }
}
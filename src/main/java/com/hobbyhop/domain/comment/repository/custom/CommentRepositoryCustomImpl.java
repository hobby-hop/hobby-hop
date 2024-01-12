package com.hobbyhop.domain.comment.repository.custom;

import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.comment.dto.*;
import com.hobbyhop.global.request.SortStandardRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CommentListResponseDTO findAllByPostId(Pageable pageable, SortStandardRequest standard, Long postId) {
        List<CommentResponseDTO> query = jpaQueryFactory
                .select(
                        Projections.constructor(
                                CommentResponseDTO.class,
                                comment.content,
                                user.username,
                                commentUser.count().intValue(),
                                comment.createdAt
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

        switch (standard.getSortStandard()){
            case 1:
                if (standard.isDesc()) {
                    query.sort(Comparator.comparing(CommentResponseDTO::getLike));
                } else {
                    query.sort(Comparator.comparing(CommentResponseDTO::getLike).reversed());
                }
                break;
                // 대댓글 적용 후 추가 예정
//            case 2:
//                break;
            default:
                if (standard.isDesc()) {
                    query.sort(Comparator.comparing(CommentResponseDTO::getCreatedAt));
                } else {
                    query.sort(Comparator.comparing(CommentResponseDTO::getCreatedAt).reversed());
                }
                break;
        }

        List<CommentResponseDTO> paging = new ArrayList<>();

        for(int i = (pageable.getPageNumber()-1) * pageable.getPageSize(); i < Long.valueOf(pageable.getOffset()).intValue(); i++){
            paging.add(query.get(i));
        }

        //Page<CommentResponseDTO> data = new PageImpl<>(paging, pageable, query.size());

        return CommentListResponseDTO.builder()
                .page(pageable.getPageNumber())
                .totalCount(query.size())
                .data(paging)
                .build();
    }
}

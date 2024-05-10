package com.hobbyhop.domain.comment.repository.custom.impl;

import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;
import static com.hobbyhop.domain.user.entity.QUser.user;

import com.hobbyhop.domain.comment.dto.CommentPageRequestDTO;
import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.repository.custom.CommentRepositoryCustom;
import com.hobbyhop.domain.post.entity.Post;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CommentRepositoryCustomImpl extends QuerydslRepositorySupport implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CommentRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<Comment> findById(Long clubId, Long postId, Long commentId){
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(comment)
                .join(post).fetchJoin()
                .on(comment.post.id.eq(post.id))
                .where(comment.id.eq(commentId).and(post.id.eq(postId)).and(post.club.id.eq(clubId)))
                .fetchOne());
    }

    @Override
    public Page<CommentResponseDTO> findAllByPostId(CommentPageRequestDTO pageRequestDTO, Long postId, Long parent) {
            JPAQuery<CommentResponseDTO> query = jpaQueryFactory
                    .select(
                            Projections.constructor(
                                    CommentResponseDTO.class,
                                    comment.content,
                                    user.username,
                                    comment.likeCnt,
                                    comment.createdAt,
                                comment.id
                        )
                )
                .from(comment)
                .where(comment.post.id.eq(postId), eqParentId(parent));

        Pageable pageable = pageRequestDTO.getPageable(pageRequestDTO.getStandard());

        List<CommentResponseDTO> content = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression eqParentId(Long parent){
        if(parent == null){
            return null;
        }
        return comment.parent.id.eq(parent);
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
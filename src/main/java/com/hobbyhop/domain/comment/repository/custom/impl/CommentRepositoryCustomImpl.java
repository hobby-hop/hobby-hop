package com.hobbyhop.domain.comment.repository.custom.impl;

import static com.hobbyhop.domain.comment.entity.QComment.comment;
import static com.hobbyhop.domain.commentuser.entity.QCommentUser.commentUser;
import static com.hobbyhop.domain.post.entity.QPost.post;

import com.hobbyhop.domain.comment.dto.CommentResponseDTO;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.comment.entity.QComment;
import com.hobbyhop.domain.comment.repository.custom.CommentRepositoryCustom;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Comment> findById(Long clubId, Long postId, Long commentId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(comment)
                .where(comment.id.eq(commentId)
                        .and(post.club.id.eq(clubId))
                        .and(post.id.eq(postId)))
                .fetchOne());
    }

    @Override
    public List<CommentResponseDTO> findAllByPostId(Long postId, Long userId) {
        List<Tuple> commentTuples = jpaQueryFactory
                .select(comment,
                        JPAExpressions
                                .selectOne()
                                .from(commentUser)
                                .where(commentUser.commentUserPK.user.id.eq(userId)
                                        .and(commentUser.commentUserPK.comment.id.eq(comment.id)))
                                .exists())
                .from(comment)
                .leftJoin(comment.parent).fetchJoin()
                .leftJoin(commentUser).on(commentUser.commentUserPK.comment.id.eq(comment.id))
                .join(comment.user).fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(comment.parent.id.asc().nullsFirst(),
                        comment.createdAt.asc())
                .fetch();

        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        Map<Long, CommentResponseDTO> commentDTOHashMap = new HashMap<>();

        commentTuples.forEach(tuple -> {
            Comment comment1 = tuple.get(comment);
            Boolean isLiked = tuple.get(1, Boolean.class);

            CommentResponseDTO commentResponseDTO = CommentResponseDTO.fromEntity(comment1);
            commentResponseDTO.setLiked(isLiked);

            commentDTOHashMap.put(commentResponseDTO.getId(), commentResponseDTO);
            if (comment1.getParent() != null) {
                commentDTOHashMap.get(comment1.getParent().getId()).getReplies().add(commentResponseDTO);
            } else {
                commentResponseDTOList.add(commentResponseDTO);
            }
        });

        return commentResponseDTOList;

    }

    @Override
    public void deleteList(List<Comment> deletelist) {
        List<Long> deleteId = new ArrayList<>();
        deletelist.forEach((d) -> {
            deleteId.add(d.getId());
        });

        Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
        jpaQueryFactory.update(comment)
                .set(comment.deletedAt, ts)
                .where(comment.id.in(deleteId)).execute();

        jpaQueryFactory.delete(commentUser)
                .where(commentUser.commentUserPK.comment.id.in(deleteId)).execute();
    }
}
package com.hobbyhop.domain.commentuser.entity;

import com.hobbyhop.domain.BaseEntity;
import com.hobbyhop.domain.comment.entity.Comment;
import com.hobbyhop.domain.commentuser.pk.CommentUserPK;
import com.hobbyhop.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import java.sql.Timestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUser extends BaseEntity {
    @EmbeddedId
    private CommentUserPK commentUserPK;

    public static CommentUser buildCommentUser(Comment comment, User user) {
        return CommentUser.builder()
                .commentUserPK(CommentUserPK.builder()
                        .user(user)
                        .comment(comment)
                        .build())
                .build();
    }
}

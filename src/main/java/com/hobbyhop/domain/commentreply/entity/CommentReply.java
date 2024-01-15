package com.hobbyhop.domain.commentreply.entity;

import com.hobbyhop.domain.commentreply.pk.CommentReplyPK;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReply {

    @EmbeddedId
    private CommentReplyPK commentReplyPK;
}

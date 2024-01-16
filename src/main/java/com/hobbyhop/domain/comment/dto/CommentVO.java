package com.hobbyhop.domain.comment.dto;

import com.hobbyhop.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentVO {
    String content;
    String writer;
    int like;
    Timestamp createdAt;
    Long id;
    Long parent;
}

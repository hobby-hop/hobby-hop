package com.hobbyhop.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class CommentVO {
    String content;
    String writer;
    int like;
    Timestamp createdAt;
    Long id;
    Long parent;
}

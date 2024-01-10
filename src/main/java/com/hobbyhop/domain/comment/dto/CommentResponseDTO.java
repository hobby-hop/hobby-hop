package com.hobbyhop.domain.comment.dto;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDTO {
    String content;
    String writer;
    int like;
    Timestamp createdAt;
}

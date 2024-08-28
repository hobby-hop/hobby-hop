package com.hobbyhop.domain.comment.dto;

import com.hobbyhop.domain.comment.entity.Comment;
import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentResponseDTO {
    String content;
    String writer;
    Long likeCnt;
    Timestamp createdAt;
    Long id;

    public static CommentResponseDTO fromEntity(Comment comment){
        return CommentResponseDTO.builder()
                .content(comment.getContent())
                .writer(comment.getUser().getUsername())
                .likeCnt(comment.getLikeCnt())
                .createdAt(comment.getCreatedAt())
                .id(comment.getId())
                .build();
    }
}

package com.hobbyhop.domain.comment.dto;

import com.hobbyhop.domain.comment.entity.Comment;
import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    private String writer;
    private Long likeCnt;
    private Timestamp createdAt;

    public static CommentResponseDTO fromEntity(Comment comment){
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getUser().getUsername())
                .likeCnt(comment.getLikeCnt())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}

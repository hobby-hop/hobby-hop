package com.hobbyhop.domain.comment.dto;

import com.hobbyhop.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class CommentResponseDTO {
    String content;
    String writer;
    Long like;
    Timestamp createdAt;
    Long id;

    public static CommentResponseDTO buildDTO(Comment comment){
        return CommentResponseDTO.builder()
                .content(comment.getContent())
                .writer(comment.getUser().getUsername())
                .like(comment.getLinkCnt())
                .createdAt(comment.getCreatedAt())
                .id(comment.getId())
                .build();
    }
}

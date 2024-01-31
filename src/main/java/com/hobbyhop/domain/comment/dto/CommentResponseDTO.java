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
    int like;
    Timestamp createdAt;
    Long id;

    public static CommentResponseDTO buildDTO(Comment comment, int like){
        return CommentResponseDTO.builder()
                .content(comment.getContent())
                .writer(comment.getUser().getUsername())
                .like(like)
                .createdAt(comment.getCreatedAt())
                .id(comment.getId())
                .build();
    }
}

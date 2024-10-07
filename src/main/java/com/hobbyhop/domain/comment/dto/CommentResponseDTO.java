package com.hobbyhop.domain.comment.dto;

import com.hobbyhop.domain.comment.entity.Comment;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    private String writer;
    private Long likeCnt;
    private boolean isLiked;
    private Timestamp createdAt;
    @Builder.Default
    private List<CommentResponseDTO> replies = new ArrayList<>();

    public static CommentResponseDTO fromEntity(Comment comment){
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getUser().getUsername())
                .likeCnt(comment.getLikeCnt())
                .isLiked(false)
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public static CommentResponseDTO fromEntity(Comment comment, boolean isLiked){
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .writer(comment.getUser().getUsername())
                .likeCnt(comment.getLikeCnt())
                .isLiked(isLiked)
                .createdAt(comment.getCreatedAt())
                .build();
    }
}

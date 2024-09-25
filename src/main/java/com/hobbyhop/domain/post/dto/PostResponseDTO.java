package com.hobbyhop.domain.post.dto;

import com.hobbyhop.domain.post.entity.Post;
import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {
    private Long clubId;
    private Long postId;
    private String title;
    private String content;
    private String writer;
    private Long likeCnt;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public static PostResponseDTO fromEntity(Post savedPost) {
        return PostResponseDTO.builder()
                .clubId(savedPost.getClub().getId())
                .postId(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .writer(savedPost.getUser().getUsername())
                .likeCnt(savedPost.getLikeCnt())
                .createdAt(savedPost.getCreatedAt())
                .modifiedAt(savedPost.getModifiedAt())
                .build();
    }
}

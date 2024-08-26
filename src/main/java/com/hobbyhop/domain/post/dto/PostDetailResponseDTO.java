package com.hobbyhop.domain.post.dto;

import com.hobbyhop.domain.post.entity.Post;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponseDTO {
    private Long clubId;
    private Long postId;
    private String writer;
    private String title;
    private String content;
    private String originImageUrl;
    private String savedImageUrl;
    private Long likeCnt;
    private boolean isLiked;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public static PostDetailResponseDTO fromEntity(Post savedPost, boolean isLiked) {
        return PostDetailResponseDTO.builder()
                .clubId(savedPost.getClub().getId())
                .postId(savedPost.getId())
                .writer(savedPost.getUser().getUsername())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .originImageUrl(savedPost.getOriginImageUrl())
                .savedImageUrl(savedPost.getSavedImageUrl())
                .likeCnt(savedPost.getLikeCnt())
                .isLiked(isLiked)
                .createdAt(savedPost.getCreatedAt())
                .modifiedAt(savedPost.getModifiedAt())
                .build();
    }
}

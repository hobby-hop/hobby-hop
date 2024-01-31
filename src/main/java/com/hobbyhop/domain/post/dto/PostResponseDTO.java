package com.hobbyhop.domain.post.dto;

import com.hobbyhop.domain.post.entity.Post;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {

    private Long clubId;
    private Long postId;
    private String writer;
    private String postTitle;
    private String postContent;
    private String originImageUrl;
    private String savedImageUrl;
    private Long likeCnt;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public static PostResponseDTO fromEntity(Post savedPost) {
        return PostResponseDTO.builder()
                .clubId(savedPost.getClub().getId())
                .postId(savedPost.getId())
                .writer(savedPost.getUser().getUsername())
                .postTitle(savedPost.getPostTitle())
                .postContent(savedPost.getPostContent())
                .originImageUrl(savedPost.getOriginImageUrl())
                .savedImageUrl(savedPost.getSavedImageUrl())
                .likeCnt(savedPost.getLikeCnt())
                .createdAt(savedPost.getCreatedAt())
                .modifiedAt(savedPost.getModifiedAt())
                .build();
    }
}

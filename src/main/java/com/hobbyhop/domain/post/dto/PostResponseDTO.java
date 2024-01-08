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

    private Long postId;
    private String postTitle;
    private String postContent;
    private String imageUrl;
    private Timestamp createAt;
    private Timestamp modifiedAt;

    public PostResponseDTO(Post savedPost) {
        this.postId = savedPost.getId();
        this.postTitle = savedPost.getPostTitle();
        this.postContent = savedPost.getPostContent();
        this.imageUrl = savedPost.getImageUrl();
        this.createAt = savedPost.getCreatedAt();
    }

    public PostResponseDTO getDto(Post savedPost) {
        return PostResponseDTO.builder()
                .postId(savedPost.getId())
                .postTitle(savedPost.getPostTitle())
                .postContent(savedPost.getPostContent())
                .imageUrl(savedPost.getImageUrl())
                .createAt(savedPost.getCreatedAt())
                .modifiedAt(savedPost.getModifiedAt())
                .build();
    }
}

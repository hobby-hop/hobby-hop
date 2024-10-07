package com.hobbyhop.domain.post.dto;

import com.hobbyhop.domain.post.entity.Post;
import com.hobbyhop.domain.postimage.dto.PostImageDTO;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<PostImageDTO> postImages;
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
                .postImages(PostImageDTO.fromEntity(savedPost.getImageSet()))
                .likeCnt(savedPost.getLikeCnt())
                .isLiked(isLiked)
                .createdAt(savedPost.getCreatedAt())
                .modifiedAt(savedPost.getModifiedAt())
                .build();
    }
}

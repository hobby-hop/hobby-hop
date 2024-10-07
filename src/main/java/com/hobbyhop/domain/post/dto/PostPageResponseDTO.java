package com.hobbyhop.domain.post.dto;

import com.hobbyhop.domain.post.entity.Post;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostPageResponseDTO {
    private Long clubId;
    private Long postId;
    private String title;
    private String writer;
    private Long likeCnt;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public static PostPageResponseDTO fromEntity(Post savedPost) {
        return PostPageResponseDTO.builder()
                .clubId(savedPost.getClub().getId())
                .postId(savedPost.getId())
                .writer(savedPost.getUser().getUsername())
                .title(savedPost.getTitle())
                .likeCnt(savedPost.getLikeCnt())
                .createdAt(savedPost.getCreatedAt())
                .modifiedAt(savedPost.getModifiedAt())
                .build();
    }
}
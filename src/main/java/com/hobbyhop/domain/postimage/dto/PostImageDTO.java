package com.hobbyhop.domain.postimage.dto;

import com.hobbyhop.domain.postimage.entity.PostImage;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostImageDTO {
    private String fileName;
    private String savedFileName;
    private String savedFileUrl;

    public static List<PostImageDTO> fromEntity(Set<PostImage> postImageSet) {
        return postImageSet.stream().map(image -> {
            PostImageDTO postImageDTO = PostImageDTO.builder()
                    .fileName(image.getFileName())
                    .savedFileName(image.getUuid() + "_" + image.getFileName())
                    .savedFileUrl(image.getSavedFileUrl())
                    .build();

            return postImageDTO;
        }).collect(Collectors.toList());

    }
}




package com.hobbyhop.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {

    @NotBlank
    private String postTitle;

    @NotBlank
    private String postContent;

    private String originImageUrl;
    private String savedImageUrl;
}

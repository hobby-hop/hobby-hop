package com.hobbyhop.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {
    @NotBlank
    @Size(min = 3, max = 50, message = "최소 3자 이상이여야 합니다.")
    private String postTitle;

    @NotBlank
    @Size(max = 500, message = "최대 500자 입니다.")
    private String postContent;
}

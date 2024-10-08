package com.hobbyhop.domain.post.dto;

import com.hobbyhop.domain.postimage.dto.PostImageDTO;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostModifyRequestDTO {
    @Size(min = 3, max = 50, message = "최소 3자 이상이여야 합니다.")
    private String title;

    @Size(max = 500, message = "최대 500자 입니다.")
    private String content;
    private List<PostImageDTO> postImages;
}

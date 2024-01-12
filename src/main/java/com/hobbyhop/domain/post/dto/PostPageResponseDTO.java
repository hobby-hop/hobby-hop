package com.hobbyhop.domain.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostPageResponseDTO {

    int page;
    Long totalCount;
    List<PostResponseDTO> data;
}

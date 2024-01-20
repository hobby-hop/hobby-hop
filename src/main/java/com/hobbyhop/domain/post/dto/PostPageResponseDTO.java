package com.hobbyhop.domain.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
@Data
@Builder
public class PostPageResponseDTO {

    int page;
    Long totalCount;
    List<PostResponseDTO> data;
}

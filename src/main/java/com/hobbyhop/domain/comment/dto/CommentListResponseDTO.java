package com.hobbyhop.domain.comment.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentListResponseDTO {
    int page;
    int totalCount;
    List<CommentResponseDTO> data;
}

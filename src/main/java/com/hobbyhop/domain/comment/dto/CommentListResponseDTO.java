package com.hobbyhop.domain.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentListResponseDTO {
    int page;
    int totalCount;
    List<CommentResponseDTO> data;
}

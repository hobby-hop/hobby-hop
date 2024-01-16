package com.hobbyhop.domain.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class CommentListResponseDTO {
    int page;
    int totalCount;
    List<CommentResponseDTO> data;
}

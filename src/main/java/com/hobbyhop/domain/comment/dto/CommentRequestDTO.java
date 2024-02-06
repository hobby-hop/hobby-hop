package com.hobbyhop.domain.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequestDTO {
    @Size(max = 200)
    String content;
}

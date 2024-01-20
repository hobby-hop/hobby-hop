package com.hobbyhop.domain.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDTO {
    @Size(max = 200)
    String content;
}

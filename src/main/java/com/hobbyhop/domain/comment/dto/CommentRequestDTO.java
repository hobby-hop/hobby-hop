package com.hobbyhop.domain.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
public class CommentRequestDTO {
    @Size(max = 200)
    String content;
}

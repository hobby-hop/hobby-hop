package com.hobbyhop.domain.comment.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class CommentRequestDTO {
    @Pattern(regexp = "^{200}$")
    String content;
}

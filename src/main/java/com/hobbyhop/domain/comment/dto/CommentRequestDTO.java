package com.hobbyhop.domain.comment.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CommentRequestDTO {
    @Pattern(regexp = "^{200}$")
    String content;
}

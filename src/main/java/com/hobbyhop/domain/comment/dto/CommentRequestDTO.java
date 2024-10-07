package com.hobbyhop.domain.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {
    @Size(max = 200)
    private String content;
}

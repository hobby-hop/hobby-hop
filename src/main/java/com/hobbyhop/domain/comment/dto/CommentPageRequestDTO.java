package com.hobbyhop.domain.comment.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@SuperBuilder
public class CommentPageRequestDTO extends PageRequestDTO {
    private String sortBy;
}

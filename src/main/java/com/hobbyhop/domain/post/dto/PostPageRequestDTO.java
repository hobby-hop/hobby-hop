package com.hobbyhop.domain.post.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class PostPageRequestDTO extends PageRequestDTO {
    private String keyword;
}

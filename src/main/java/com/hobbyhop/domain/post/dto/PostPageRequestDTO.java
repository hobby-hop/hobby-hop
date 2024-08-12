package com.hobbyhop.domain.post.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class PostPageRequestDTO extends PageRequestDTO {
    private String keyword;
}

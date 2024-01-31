package com.hobbyhop.domain.post.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Getter;

@Getter
public class PostPageRequestDTO extends PageRequestDTO {
    private String keyword;
}

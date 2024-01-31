package com.hobbyhop.domain.comment.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Getter;

@Getter
public class CommentPageRequestDTO extends PageRequestDTO {
    private String standard;
}

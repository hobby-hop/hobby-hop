package com.hobbyhop.domain.club.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Getter;

@Getter
public class ClubPageRequestDTO extends PageRequestDTO {
    private String keyword;
    private Long category;
}

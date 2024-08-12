package com.hobbyhop.domain.club.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ClubPageRequestDTO extends PageRequestDTO {
    private String keyword;
    private Long categoryId;
}

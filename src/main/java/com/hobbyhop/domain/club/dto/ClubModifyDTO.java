package com.hobbyhop.domain.club.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubModifyDTO {

    @Size(min = 3, message = "최소 3자 이상이여야 합니다.")
    private String title;
    private String content;
    private Long categoryId;
}
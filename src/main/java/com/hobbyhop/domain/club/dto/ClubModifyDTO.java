package com.hobbyhop.domain.club.dto;

import jakarta.validation.constraints.Min;
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
    @Size(message = "모임 명은 3자 이상 30자 이하로 입력해주세요")
    private String title;

    @Size(min = 3, max = 255, message = "모임 소개는 3자 이상 255자 이하로 입력해주세요")
    private String content;

    @Min(value = 1, message = "categoryId는 1 이상이어야 합니다.")
    private Long categoryId;
}
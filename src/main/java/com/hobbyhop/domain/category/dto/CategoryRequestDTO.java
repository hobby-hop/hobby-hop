package com.hobbyhop.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {
    @NotBlank(message = "카테고리 명을 입력해주세요.")
    private String categoryName;
    @NotBlank(message = "모임 소개를 입력하주세요.")
    private String description;
}

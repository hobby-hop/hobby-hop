package com.hobbyhop.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestDTO {
    @NotBlank(message = "카테고리 명을 입력해주세요")
    private String categoryName;
    @NotBlank(message = "설명을 입력해주세요")
    private String description;
}

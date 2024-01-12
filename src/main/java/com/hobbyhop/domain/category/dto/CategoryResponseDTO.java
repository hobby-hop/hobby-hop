package com.hobbyhop.domain.category.dto;

import com.hobbyhop.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDTO {
    private String categoryName;
    private String description;

    public static CategoryResponseDTO fromEntity(Category category) {
        return CategoryResponseDTO.builder()
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }
}

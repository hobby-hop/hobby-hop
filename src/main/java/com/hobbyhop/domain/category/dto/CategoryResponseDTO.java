package com.hobbyhop.domain.category.dto;

import com.hobbyhop.domain.category.entity.Category;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

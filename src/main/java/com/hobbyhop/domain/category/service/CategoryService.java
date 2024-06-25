package com.hobbyhop.domain.category.service;

import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
import com.hobbyhop.domain.category.dto.CategoryResponseDTO;
import com.hobbyhop.domain.category.entity.Category;

public interface CategoryService {
    CategoryResponseDTO makeCategory(CategoryRequestDTO categoryRequestDTO);

    void removeCategory(Long categoryId);

    Category findCategory(Long categoryId);
}

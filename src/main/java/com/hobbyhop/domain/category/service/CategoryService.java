package com.hobbyhop.domain.category.service;

import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
import com.hobbyhop.domain.category.dto.CategoryResponseDTO;
import com.hobbyhop.domain.category.entity.Category;

public interface CategoryService {
    // TODO : implement 관리자가 카테고리를 추가할 수 있는 기능

    CategoryResponseDTO makeCategory(CategoryRequestDTO categoryRequestDTO);
    void removeCategory(Long categoryId);
    Category findCategory(Long categoryId);
}

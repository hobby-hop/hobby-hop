package com.hobbyhop.domain.category.service.impl;

import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
import com.hobbyhop.domain.category.dto.CategoryResponseDTO;
import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.repository.CategoryRepository;
import com.hobbyhop.domain.category.service.CategoryService;
import com.hobbyhop.global.exception.category.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    @Transactional
    public CategoryResponseDTO makeCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = Category.builder()
                .categoryName(categoryRequestDTO.getCategoryName())
                .description(categoryRequestDTO.getDescription())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponseDTO.fromEntity(savedCategory);
    }

    @Override
    @Transactional
    public void removeCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }
}

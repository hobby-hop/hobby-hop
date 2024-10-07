package com.hobbyhop.domain.category.service.impl;

import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
import com.hobbyhop.domain.category.dto.CategoryResponseDTO;
import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.repository.CategoryRepository;
import com.hobbyhop.domain.category.service.CategoryService;
import com.hobbyhop.global.exception.category.AlreadyExistCategoryException;
import com.hobbyhop.global.exception.category.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponseDTO makeCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRepository.existsByCategoryName(categoryRequestDTO.getCategoryName())) {
            throw new AlreadyExistCategoryException();
        }

        Category category = Category.buildCategory(categoryRequestDTO);

        return CategoryResponseDTO.fromEntity(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void removeCategory(Long categoryId) {
        categoryRepository.deleteAllElement(categoryId);
    }

    @Override
    public Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }
}

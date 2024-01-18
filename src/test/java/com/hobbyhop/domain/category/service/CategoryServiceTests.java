package com.hobbyhop.domain.category.service;

import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
import com.hobbyhop.domain.category.dto.CategoryResponseDTO;
import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.repository.CategoryRepository;
import com.hobbyhop.domain.category.service.impl.CategoryServiceImpl;
import com.hobbyhop.test.CategoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Category]")
public class CategoryServiceTests implements CategoryTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    private Category category;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(TEST_CATEGORY_ID)
                .categoryName(TEST_CATEGORY_NAME)
                .description(TEST_CATEGORY_DESCRIPTION)
                .build();

        categoryRequestDTO = CategoryRequestDTO.builder()
                .categoryName(TEST_CATEGORY_NAME)
                .description(TEST_CATEGORY_DESCRIPTION)
                .build();

        categoryResponseDTO = CategoryResponseDTO.fromEntity(category);
    }

    @DisplayName("[Create]")
    @Test
    void category_생성_성공() {
        given(categoryRepository.save(any())).willReturn(category);

        assertThat(categoryService.makeCategory(categoryRequestDTO)).isEqualTo(categoryResponseDTO);
    }

    @DisplayName("[Delete]")
    @Test
    void category_삭제_성공() {
        categoryService.removeCategory(TEST_CATEGORY_ID);
        verify(categoryRepository, times(1)).deleteById(TEST_CATEGORY_ID);
    }
}

package com.hobbyhop.domain.category.service.impl;

import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
import com.hobbyhop.domain.category.dto.CategoryResponseDTO;
import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.repository.CategoryRepository;
import com.hobbyhop.global.exception.category.AlreadyExistCategoryException;
import com.hobbyhop.test.CategoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Category]")
public class CategoryServiceImplTests implements CategoryTest {
    @InjectMocks
    private CategoryServiceImpl sut;
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

    @DisplayName("카테고리 생성 성공")
    @Test
    void category_생성_성공() {
        // Given
        given(categoryRepository.save(any())).willReturn(category);

        // When & Then
        assertThat(sut.makeCategory(categoryRequestDTO)).isEqualTo(categoryResponseDTO);
    }
    @DisplayName("카테고리 생성 실패")
    @Test
    void category_생성_실패() {
        // Given
        given(categoryRepository.existsByCategoryName(TEST_CATEGORY_NAME)).willReturn(true);

        // When & Then
        assertThatCode(() -> sut.makeCategory(categoryRequestDTO)).isInstanceOf(AlreadyExistCategoryException.class);
    }
}

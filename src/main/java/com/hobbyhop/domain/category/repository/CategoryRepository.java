package com.hobbyhop.domain.category.repository;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.repository.custom.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
    boolean existsByCategoryName(String categoryName);
}

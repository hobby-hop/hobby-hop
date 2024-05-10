package com.hobbyhop.domain.category.repository;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.repository.custom.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>,
        CategoryRepositoryCustom {
    Optional<Category> findByCategoryName(String categoryName);
    boolean existsByCategoryName(String categoryName);
}

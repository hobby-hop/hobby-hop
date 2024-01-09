package com.hobbyhop.domain.category.repository;

import com.hobbyhop.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

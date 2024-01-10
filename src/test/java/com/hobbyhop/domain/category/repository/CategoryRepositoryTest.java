package com.hobbyhop.domain.category.repository;

import com.hobbyhop.domain.category.entity.Category;
import com.hobbyhop.domain.category.enums.HobbyCategory;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class CategoryRepositoryTest {
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Test
//    void generateDummyData() {
//        Category category = Category.builder()
//                .hobbyCategory(HobbyCategory.MUSIC)
//                .build();
//
//        log.info(categoryRepository.save(category).getHobbyCategory());
//    }
}
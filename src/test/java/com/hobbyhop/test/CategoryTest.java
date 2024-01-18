package com.hobbyhop.test;

import com.hobbyhop.domain.category.entity.Category;

public interface CategoryTest extends UserTest {

    Long TEST_CATEGORY_ID = 1L;
    String TEST_CATEGORY_NAME = "category";
    String TEST_CATEGORY_DESCRIPTION = "description";

    Long TEST_OTHER_CATEGORY_ID = 2L;
    String TEST_OTHER_CATEGORY_NAME = "other";
    String TEST_OTHER_CATEGORY_DESCRIPTION = "otherDescription";

    Category TEST_CATEGORY =
            Category.builder()
                    .id(TEST_CATEGORY_ID)
                    .categoryName(TEST_CATEGORY_NAME)
                    .description(TEST_CATEGORY_DESCRIPTION)
                    .build();

    Category TEST_OTHER_CATEGORY =
            Category.builder()
                    .id(TEST_OTHER_CATEGORY_ID)
                    .categoryName(TEST_OTHER_CATEGORY_NAME)
                    .description(TEST_OTHER_CATEGORY_DESCRIPTION)
                    .build();
}

package com.hobbyhop.domain.category.enums;

public enum HobbyCategory {
    SPORTS(1L),
    MUSIC(2L),
    ARTS_CRAFTS(3L),
    COOKING(4L);

    private final Long categoryId;

    HobbyCategory(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}

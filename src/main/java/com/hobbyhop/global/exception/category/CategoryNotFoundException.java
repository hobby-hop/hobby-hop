package com.hobbyhop.global.exception.category;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException() {
        super(ErrorCode.NOT_FOUND_CATEGORY_EXCEPTION);
    }
}

package com.hobbyhop.global.exception.category;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class AlreadyExistCategoryException extends BusinessException {
    public AlreadyExistCategoryException() {
        super(ErrorCode.ALREADY_EXIST_CATEGORY_NAME_EXCEPTION);
    }
}

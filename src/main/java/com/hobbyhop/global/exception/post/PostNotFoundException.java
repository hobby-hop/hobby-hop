    package com.hobbyhop.global.exception.post;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class PostNotFoundException extends BusinessException {

    public PostNotFoundException() {
        super(ErrorCode.NOT_FOUND_POST_EXCEPTION);
    }
}

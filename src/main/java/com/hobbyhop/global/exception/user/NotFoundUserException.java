package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class NotFoundUserException extends BusinessException {
    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER_EXCEPTION);
    }
}

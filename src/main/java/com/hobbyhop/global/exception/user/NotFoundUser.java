package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class NotFoundUser extends BusinessException {
    public NotFoundUser() {
        super(ErrorCode.NOT_FOUND_USER_EXCEPTION);
    }
}

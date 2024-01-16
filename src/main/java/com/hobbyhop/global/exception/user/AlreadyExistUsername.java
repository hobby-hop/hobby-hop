package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class AlreadyExistUsername extends BusinessException {
    public AlreadyExistUsername() {
        super(ErrorCode.ALREADY_EXIST_USERNAME_EXCEPTION);
    }
}

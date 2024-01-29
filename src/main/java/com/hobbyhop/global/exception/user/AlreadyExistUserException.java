package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class AlreadyExistUserException extends BusinessException {

    public AlreadyExistUserException() {
        super(ErrorCode.ALREADY_EXIST_USER_EXCEPTION);
    }
}
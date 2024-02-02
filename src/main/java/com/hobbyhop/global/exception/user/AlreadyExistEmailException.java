package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class AlreadyExistEmailException extends BusinessException {
    public AlreadyExistEmailException() {
        super(ErrorCode.ALREADY_EXIST_EMAIL_EXCEPTION);
    }
}

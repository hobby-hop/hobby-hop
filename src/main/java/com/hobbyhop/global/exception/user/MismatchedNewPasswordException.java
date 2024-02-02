package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class MismatchedNewPasswordException extends BusinessException {
    public MismatchedNewPasswordException() {
        super(ErrorCode.MISMATCHED_PASSWORD_EXCEPTION);
    }
}

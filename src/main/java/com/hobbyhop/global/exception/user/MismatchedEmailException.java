package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class MismatchedEmailException extends BusinessException {
    public MismatchedEmailException() {
        super(ErrorCode.MISMATCHED_EMAIL_EXCEPTION);
    }
}

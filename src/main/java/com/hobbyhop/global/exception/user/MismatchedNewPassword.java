package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class MismatchedNewPassword extends BusinessException {
    public MismatchedNewPassword() {
        super(ErrorCode.MISMATCHED_PASSWORD_EXCEPTION);
    }
}

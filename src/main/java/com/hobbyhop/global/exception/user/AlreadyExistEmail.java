package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class AlreadyExistEmail extends BusinessException {
    public AlreadyExistEmail() {
        super(ErrorCode.ALREADY_EXIST_EMAIL_EXCEPTION);
    }
}

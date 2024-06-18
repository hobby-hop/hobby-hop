package com.hobbyhop.global.exception.auth;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class UnAuthorizedModifyException extends BusinessException {

    public UnAuthorizedModifyException() {
        super(ErrorCode.UNAUTHORIZED_MODIFY_EXCEPTION);
    }
}

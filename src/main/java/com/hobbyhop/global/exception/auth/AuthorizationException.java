package com.hobbyhop.global.exception.auth;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class AuthorizationException extends BusinessException {

    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}

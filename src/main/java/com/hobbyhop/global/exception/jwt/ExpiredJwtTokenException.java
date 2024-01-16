package com.hobbyhop.global.exception.jwt;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class ExpiredJwtTokenException extends BusinessException {
    public ExpiredJwtTokenException() {
        super(ErrorCode.EXPIRED_JWT_TOKEN_EXCEPTION);
    }
}

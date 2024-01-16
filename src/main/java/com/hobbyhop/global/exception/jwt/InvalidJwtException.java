package com.hobbyhop.global.exception.jwt;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class InvalidJwtException extends BusinessException {
    public InvalidJwtException() {
        super(ErrorCode.INVALID_JWT_EXCEPTION);
    }
}

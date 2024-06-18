package com.hobbyhop.global.exception.jwt;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class NoJwtException extends BusinessException {
    public NoJwtException() {
        super(ErrorCode.NO_JWT_EXCEPTION);
    }
}

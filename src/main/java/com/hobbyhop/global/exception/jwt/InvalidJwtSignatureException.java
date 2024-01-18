package com.hobbyhop.global.exception.jwt;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class InvalidJwtSignatureException extends BusinessException {
    public InvalidJwtSignatureException() {
        super(ErrorCode.INVALID_JWT_SIGNATURE_EXCEPTION);
    }
}

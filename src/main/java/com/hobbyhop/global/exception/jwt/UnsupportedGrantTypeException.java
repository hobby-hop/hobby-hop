package com.hobbyhop.global.exception.jwt;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class UnsupportedGrantTypeException extends BusinessException {
    public UnsupportedGrantTypeException() {
        super(ErrorCode.NOT_SUPPORTED_GRANT_TYPE_EXCEPTION);
    }
}

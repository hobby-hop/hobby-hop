package com.hobbyhop.global.exception.joinrequest;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class NoSuchRequestException extends BusinessException {
    public NoSuchRequestException() {
        super(ErrorCode.NO_SUCH_REQUEST_EXCEPTION);
    }
}

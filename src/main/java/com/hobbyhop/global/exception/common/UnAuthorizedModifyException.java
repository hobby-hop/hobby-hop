package com.hobbyhop.global.exception.common;

public class UnAuthorizedModifyException extends BusinessException{

    public UnAuthorizedModifyException(ErrorCode errorCode) {
        super(errorCode);
    }
}

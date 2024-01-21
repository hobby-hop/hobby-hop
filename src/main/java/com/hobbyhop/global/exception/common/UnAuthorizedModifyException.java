package com.hobbyhop.global.exception.common;

public class UnAuthorizedModifyException extends BusinessException{

    public UnAuthorizedModifyException() {
        super(ErrorCode.UNAUTHORIZED_MODIFY_EXCEPTION);
    }
}

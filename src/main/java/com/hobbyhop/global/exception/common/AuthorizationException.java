package com.hobbyhop.global.exception.common;

public class AuthorizationException extends BusinessException{

    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}

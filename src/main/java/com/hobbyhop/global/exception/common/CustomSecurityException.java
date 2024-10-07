package com.hobbyhop.global.exception.common;

import lombok.Getter;

@Getter
public class CustomSecurityException extends RuntimeException{
    private int status;

    public CustomSecurityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }
}

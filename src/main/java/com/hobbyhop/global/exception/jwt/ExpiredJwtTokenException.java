package com.hobbyhop.global.exception.jwt;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class ExpiredJwtTokenException extends BusinessException {
    // TODO 나중에 에러코드 이넘 고치기
    public ExpiredJwtTokenException() {
        super(ErrorCode.NO_AUTHORIZATION_EXCEPTION);
    }
}

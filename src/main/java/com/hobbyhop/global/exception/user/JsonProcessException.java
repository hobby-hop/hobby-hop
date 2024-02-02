package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class JsonProcessException extends BusinessException {
    public JsonProcessException() {
        super(ErrorCode.JSON_PROCESSING_EXCEPTION);
    }
}

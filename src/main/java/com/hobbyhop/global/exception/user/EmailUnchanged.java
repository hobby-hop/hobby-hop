package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class EmailUnchanged extends BusinessException {
    public EmailUnchanged() {
        super(ErrorCode.EMAIL_UNCHANGED_EXCEPTION);
    }
}

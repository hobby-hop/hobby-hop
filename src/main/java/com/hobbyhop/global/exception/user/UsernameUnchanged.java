package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class UsernameUnchanged extends BusinessException {
    public UsernameUnchanged() {
        super(ErrorCode.USERNAME_UNCHANGED_EXCEPTION);
    }
}

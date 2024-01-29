package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class NotAvailableUsernameException extends BusinessException {
    public NotAvailableUsernameException() {
        super(ErrorCode.NOT_AVAILABLE_USERNAME_EXCEPTION);
    }
}

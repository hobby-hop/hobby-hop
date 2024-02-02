package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class NotAvailableEmailException extends BusinessException {
    public NotAvailableEmailException() {
        super(ErrorCode.NOT_AVAILABLE_EMAIL_EXCEPTION);
    }
}

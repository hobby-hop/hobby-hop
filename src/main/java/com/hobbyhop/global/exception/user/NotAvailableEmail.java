package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class NotAvailableEmail extends BusinessException {
    public NotAvailableEmail() {
        super(ErrorCode.NOT_AVAILABLE_EMAIL);
    }
}

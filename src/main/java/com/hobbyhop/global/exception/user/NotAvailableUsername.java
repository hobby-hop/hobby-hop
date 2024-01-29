package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class NotAvailableUsername extends BusinessException {
    public NotAvailableUsername() {
        super(ErrorCode.NOT_AVAILABLE_USERNAME);
    }
}

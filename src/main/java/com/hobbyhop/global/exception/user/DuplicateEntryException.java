package com.hobbyhop.global.exception.user;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class DuplicateEntryException extends BusinessException {
    public DuplicateEntryException() {
        super(ErrorCode.DUPLICATE_ENTRY_EXCEPTION);
    }
}

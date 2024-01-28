package com.hobbyhop.global.exception.joinrequest;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class PendingRequest extends BusinessException {
    public PendingRequest() {
        super(ErrorCode.PENDING_REQUEST);
    }
}

package com.hobbyhop.global.exception.joinrequest;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class PendingRequestException extends BusinessException {
    public PendingRequestException() {super(ErrorCode.PENDING_REQUEST);}
}

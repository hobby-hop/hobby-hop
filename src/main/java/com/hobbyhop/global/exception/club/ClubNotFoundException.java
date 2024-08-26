package com.hobbyhop.global.exception.club;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class ClubNotFoundException extends BusinessException {
    public ClubNotFoundException() {
        super(ErrorCode.NOT_FOUND_CLUB_EXCEPTION);
    }
}

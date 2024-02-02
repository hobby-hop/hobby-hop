package com.hobbyhop.global.exception.club;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class AlreadyExistClubTitle extends BusinessException {
    public AlreadyExistClubTitle() {
        super(ErrorCode.ALREADY_CLUB_TITLE_EXIST_EXCEPTION);
    }
}

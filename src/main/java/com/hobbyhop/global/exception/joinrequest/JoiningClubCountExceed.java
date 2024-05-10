package com.hobbyhop.global.exception.joinrequest;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class JoiningClubCountExceed extends BusinessException {
    public JoiningClubCountExceed() {
        super(ErrorCode.JOINING_CLUB_COUNT_EXCEEd);
    }
}

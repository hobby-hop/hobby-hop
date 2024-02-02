package com.hobbyhop.global.exception.clubmember;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class ClubMemberNotFoundException extends BusinessException {
    public ClubMemberNotFoundException() {
        super(ErrorCode.NOT_FOUND_CLUB_MEMBER_EXCEPTION);
    }
}

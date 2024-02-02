package com.hobbyhop.global.exception.clubmember;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class ClubMemberAlreadyJoined extends BusinessException {
    public ClubMemberAlreadyJoined() {
        super(ErrorCode.CLUB_MEMBER_ALREADY_JOINED_EXCEPTION);
    }
}

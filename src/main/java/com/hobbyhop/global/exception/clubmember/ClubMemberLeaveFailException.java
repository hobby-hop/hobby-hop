package com.hobbyhop.global.exception.clubmember;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class ClubMemberLeaveFailException extends BusinessException {
    public ClubMemberLeaveFailException() {super(ErrorCode.CLUB_MEMBER_LEAVE_FAIL_EXCEPTION);}
}

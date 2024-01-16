package com.hobbyhop.global.exception.clubmember;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class ClubMemberRoleException extends BusinessException {
    public ClubMemberRoleException() {
        super(ErrorCode.NO_PERMISSION_EXCEPTION);
    }
}

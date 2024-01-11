package com.hobbyhop.global.exception.post;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class PostNotCorrespondUser extends BusinessException {

    public PostNotCorrespondUser() {
        super(ErrorCode.NOT_CORRESPOND_USER_EXCEPTION);
    }
}
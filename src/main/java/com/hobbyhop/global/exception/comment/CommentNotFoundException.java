package com.hobbyhop.global.exception.comment;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class CommentNotFoundException extends BusinessException {
    public CommentNotFoundException() {
        super(ErrorCode.NOT_FOUND_COMMENT_EXCEPTION);
    }
}

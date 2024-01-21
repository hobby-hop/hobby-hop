package com.hobbyhop.global.exception.s3;

import com.hobbyhop.global.exception.common.BusinessException;
import com.hobbyhop.global.exception.common.ErrorCode;

public class ImageSaveException extends BusinessException {
    public ImageSaveException(){super(ErrorCode.IMAGE_SAVE_EXCEPTION);}
}

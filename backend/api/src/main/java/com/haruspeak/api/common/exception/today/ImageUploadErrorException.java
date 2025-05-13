package com.haruspeak.api.common.exception.today;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class ImageUploadErrorException extends HaruspeakException {
    public ImageUploadErrorException() {
        super(ErrorCode.IMAGE_UPLOAD_ERROR);
    }
}
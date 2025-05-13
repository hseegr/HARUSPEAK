package com.haruspeak.api.common.exception.today;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class InvalidBase64Exception extends HaruspeakException {
    public InvalidBase64Exception() {
        super(ErrorCode.INVALID_BASE64);
    }
}
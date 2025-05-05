package com.haruspeak.api.common.exception.common;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class InvalidConditionFormatException extends HaruspeakException {
    public InvalidConditionFormatException() {
        super(ErrorCode.INVALID_CONDITION_FORMAT);
    }
}
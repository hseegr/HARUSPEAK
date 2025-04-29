package com.haruspeak.api.common.exception.user;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class InvalidJwtInputException extends HaruspeakException {
    public InvalidJwtInputException() {
        super(ErrorCode.INVALID_JWT_INPUT);
    }

    public InvalidJwtInputException(String field) {
        super(ErrorCode.INVALID_JWT_INPUT, String.format("field: %s", field));
    }
}
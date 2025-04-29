package com.haruspeak.api.common.exception.user;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class InvalidTokenException extends HaruspeakException {
    public InvalidTokenException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
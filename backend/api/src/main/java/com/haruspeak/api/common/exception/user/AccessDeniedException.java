package com.haruspeak.api.common.exception.user;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;

public class AccessDeniedException extends HaruspeakException {
    public AccessDeniedException() {
        super(ErrorCode.ACCESS_DENIED);
    }
}
package com.haruspeak.api.common.exception;

import lombok.Getter;

@Getter
public class HaruspeakException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String details;

    public HaruspeakException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public HaruspeakException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage() + " | " + details);
        this.errorCode = errorCode;
        this.details = details;
    }

}


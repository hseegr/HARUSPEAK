package com.haruspeak.batch.common.exception;

import lombok.Getter;

@Getter
public class HaruspeakBatchException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String details;

    public HaruspeakBatchException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public HaruspeakBatchException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage() + " | " + details);
        this.errorCode = errorCode;
        this.details = details;
    }

}

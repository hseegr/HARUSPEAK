package com.haruspeak.batch.common.exception;

import lombok.Getter;

@Getter
public class HaruspeakBatchException extends RuntimeException {
    private final ErrorCode errorCode;

    public HaruspeakBatchException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HaruspeakBatchException(ErrorCode errorCode, Exception cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

}

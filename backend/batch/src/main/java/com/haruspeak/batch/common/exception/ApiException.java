package com.haruspeak.batch.common.exception;

public class ApiException extends HaruspeakBatchException {
    public ApiException() {
        super(ErrorCode.API_CLIENT_EXCEPTION);
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}

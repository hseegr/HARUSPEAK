package com.haruspeak.batch.common.exception;

public class DiaryThumbnailException extends HaruspeakBatchException {
    public DiaryThumbnailException() {
        super(ErrorCode.DIARY_THUMBNAIL_EXCEPTION);
    }

    public DiaryThumbnailException(ErrorCode errorCode) {
        super(errorCode);
    }
}

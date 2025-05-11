package com.haruspeak.batch.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    API_CLIENT_EXCEPTION(70001, "API 호출 중 에러가 발생했습니다."),
    API_CALL_FAILED(70002, "API 호출에 실패했습니다."),
    API_RESPONSE_FORMAT_ERROR(70003, "API 호출 결과의 포맷이 올바르지 않습니다."),

    DIARY_THUMBNAIL_EXCEPTION(80000, "썸네일 생성 작업 중 예외가 발생했습니다."),
    EMPTY_FILE_EXCEPTION(80001, "업로드할 파일이 비어있습니다."),
    NO_FILE_EXTENSION(80002, "파일에 확장자가 없습니다."),
    INVALID_FILE_EXTENSION(80003, "지원하지 않는 파일 확장자입니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(80004, "이미지 업로드 중 IO 예외가 발생했습니다."),
    PUT_OBJECT_EXCEPTION(80005, "S3에 객체를 업로드하는 중 예외가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(80006, "이미지 삭제 중 IO 예외가 발생했습니다."),

    ;
    private final int code;
    private final String message;

}

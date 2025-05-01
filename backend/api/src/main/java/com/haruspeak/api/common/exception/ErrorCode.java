package com.haruspeak.api.common.exception;
import lombok.Getter;

// 10 : user
// 20 : 오늘 일기
// 30 : 하루 일기
// 40 : 순간 일기
// 50 :

@Getter
public enum ErrorCode  {
    // 400 Bad Request
    MISSING_REQUIRED_FIELDS(40000, "요청에 필수 필드가 누락되었습니다."),
    GOOGLE_LOGIN_FAILED(40010, "구글 로그인 인증 실패. 유효한 인증 정보를 제공해 주세요. 구글에서 반환된 오류: %s"),
    INVALID_JWT_INPUT(40011, "JWT 생성에 필요한 필드가 누락되었거나 잘못되었습니다."),
    INVALID_AUDIO_FILE(40020, "음성 파일이 유효하지 않거나 잘못된 형식입니다."),
    BLANK_BASE64(40021, "Base64 문자열이 비어있습니다."),
    INVALID_BASE64(40022, "유효하지 않은 Base64 문자열입니다."),
    INVALID_CONDITION_FORMAT(40040, "유효하지 않은 조건 형식입니다."),

    // 401 Unauthorized
    UNAUTHORIZED(40100, "인증되지 않은 사용자입니다. 로그인 후 다시 시도해 주세요."),
    INVALID_TOKEN(40110, "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_FORMAT(40111, "잘못된 토큰 형식입니다. 올바른 토큰을 제공해 주세요."),
    TOKEN_EXPIRED(40112, "토큰이 만료되었습니다."),

    // 403 Forbidden
    USER_NOT_FORBIDDEN(40310, "접근이 허용되지 않은 사용자입니다."),

    // 404 Not Found
    USER_NOT_FOUND(40410, "존재하지 않는 사용자입니다."),
    DIARY_NOT_FOUND(40430, "존재하지 않는 하루 일기입니다."),
    MOMENT_NOT_FOUND(40440, "존재하지 않는 순간 일기입니다."),
    USER_TAG_NOT_FOUND(40441, "존재하지 않는 사용자 태그입니다."),

    // 409 Conflict
    MOMENT_CONFLICT(40920, "해당 시간의 일기가 이미 존재합니다."),

    // 410 Gone
    DELETED_DIARY(41030, "요청된 하루 일기는 이미 삭제되었습니다. 더 이상 사용할 수 없습니다."),
    DELETED_MOMENT(41040, "요청된 순간 일기는 이미 삭제되었습니다. 더 이상 사용할 수 없습니다."),

    // 500 Internal Server Error
    IMAGE_UPLOAD_ERROR(50020, "이미지 업로드 중 오류가 발생했습니다. 다시 시도해 주세요."), // S3 업로드 에러 세분화 시 50으로 따로
    SUMMARY_CONTENT_REGENERATION_FAILED(50030, "일기 요약 재생성 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."),

    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

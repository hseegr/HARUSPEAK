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
    NO_CONTENTS_AT_TAG_GENERATION(40041, "태그생성을 위한 컨텐츠 내용이 필요합니다. 내용을 입력해주세요."),
    DUPLICATE_TAG_VALUE(40042, "요청된 태그 리스트에 중복된 값이 있습니다. 중복을 제거하고 다시 시도해주세요"),
    INVALID_TITLE_LENGTH(40030, "제목은 50자 이하여야 합니다."),
    INVALID_CONTENT_LENGTH(40031, "내용은 200자 이하여야 합니다."),
    INVALID_MOMENT_CONTENT_LENGTH(40041, "내용은 500자 이하여야 합니다."),
    INVALID_MOMENT_TAG_SIZE(40042,"태그는 최대 10개까지 등록할 수 있습니다"),
    INVALID_MOMENT_TAG_LENGTH(40043,"태그는 10자 이내여야 합니다."),
    INVALID_MOMENT_TAG_FORMAT(40044,"태그는 빈 문자열일 수 없습니다."),
    INVALID_MOMENT_TAG_CHARACTER(40045,"태그에 허용되지 않은 특수문자가 포함되어 있습니다."),
    BLANK_MOMENT(40046,"내용이 존재해야합니다."),
    DUPLICATION_DELETE_IMAGE(40047,"이미지에 삭제할 이미지가 존재합니다."),
    DUPLICATION_TAG(40048,"태그명이 중복됩니다."),
    NO_IS_EDIT_PAGE_CONDITION(40041, "태그 자동생성 요청시 현재페이지가 수정페이지인지 아닌지에 대한 여부가 필요합니다."),

    // 401 Unauthorized
    UNAUTHORIZED(40100, "인증되지 않은 사용자입니다. 로그인 후 다시 시도해 주세요."),
    INVALID_TOKEN(40110, "유효하지 않은 토큰입니다."),
    INVALID_TOKEN_FORMAT(40111, "잘못된 토큰 형식입니다. 올바른 토큰을 제공해 주세요."),
    TOKEN_EXPIRED(40112, "토큰이 만료되었습니다."),

    // 403 Forbidden
    ACCESS_DENIED(40310, "접근이 허용되지 않은 사용자입니다."),

    // 404 Not Found
    USER_NOT_FOUND(40410, "존재하지 않는 사용자입니다."),
    DIARY_NOT_FOUND(40430, "존재하지 않는 하루 일기입니다."),
    MOMENT_NOT_FOUND(40440, "존재하지 않는 순간 일기입니다."),
    USER_TAG_NOT_FOUND(40441, "존재하지 않는 사용자 태그입니다."),

    // 409 Conflict
    MOMENT_CONFLICT(40920, "해당 시간의 일기가 이미 존재합니다."),
    THUMBNAIL_REGEN_CONFLICT(40921, "대기 중인 썸네일 재생성 요청이 이미 존재합니다."),
    THUMBNAIL_REGENERATING_CONFLICT(40922, "썸네일이 재생성 중입니다. 잠시 후에 시도해주세요."),
    RECOMMEND_TAGS_ARRAY_EMPTY(40940, "응답값인 추천태그리스트가 비어있습니다. 올바르지 않은 응답입니다."),

    // 410 Gone
    DELETED_DIARY(41030, "요청된 하루 일기는 이미 삭제되었습니다. 더 이상 사용할 수 없습니다."),
    DELETED_MOMENT(41040, "요청된 순간 일기는 이미 삭제되었습니다. 더 이상 사용할 수 없습니다."),

    // 429 Too Many Requests
    THUMBNAIL_REGEN_REQUEST_LIMIT_EXCEEDED(42920, "썸네일 재생성 최대 요청 횟수를 초과하였습니다. 더 이상 재생성이 불가능합니다."),
    THUMBNAIL_REGEN_REDIS_RETRY_COUNT_LIMIT_EXCEEDED(42921, "가능한 썸네일 redis 재생성 재시도 횟수를 초과하였습니다. 다시 요청해주세요."),
    SUMMARY_CONTENT_GENERATE_COUNT_LIMIT_EXCEEDED(42930, "요약 재생성 최대 요청 횟수를 초과하였습니다. 더 이상 재생성이 불가능합니다."),

    // 500 Internal Server Error
    USER_REGISTER_ERROR(50010, "회원가입 처리 중 오류가 발생했습니다."),
    TOKEN_SAVE_ERROR(50011, "토큰 저장 중 오류가 발생했습니다."),
    IMAGE_UPLOAD_ERROR(50020, "이미지 업로드 중 오류가 발생했습니다. 다시 시도해 주세요."), // S3 업로드 에러 세분화 시 50으로 따로
    TODAY_READ_ERROR(50021, "오늘의 일기를 불러오는 중 오류가 발생했습니다."),
    TODAY_SAVE_ERROR(50022, "오늘의 일기 저장 중 오류가 발생했습니다."),
    IMAGE_DELETE_ERROR(50023, "이미지 삭제 중 오류가 발생했습니다."),
    SUMMARY_CONTENT_REGENERATION_FAILED(50030, "일기 요약 재생성 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."),
    MOMENT_SAVE_ERROR(50040, "순간 일기 저장 중 오류가 발생했습니다."),
    MOMENT_UPDATE_ERROR(50041,"순간 일기 업데이트 중 오류가 발생했습니다."),
    MOMENT_DELETE_ERROR(50042,"순간 일기 삭제 중 오류가 발생했습니다."),
    MOMENT_READ_ERROR(50043, "순간 일기를 불러오는 중 오류가 발생했습니다."),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

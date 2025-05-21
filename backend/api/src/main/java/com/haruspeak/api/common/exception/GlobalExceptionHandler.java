package com.haruspeak.api.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * HaruspeakException 예외 처리
     */
    @ExceptionHandler(HaruspeakException .class)
    public ResponseEntity<ErrorResponse> handleHaruspeakException(HaruspeakException e) {
        log.warn("📛 HarusepakException 발생: {}", e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getCode() / 100)
                .body(createErrorResponse(
                        e.getErrorCode().getCode(),
                        e.getErrorCode().getMessage(),
                        e.getDetails())
                );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
        log.warn("📛 바인딩 실패: {}", ex.getMessage());

        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        // userTags 관련 필드 에러만 필터링
//        String message = ex.getFieldErrors().stream()
//                .filter(fieldError -> "userTags".equals(fieldError.getField()))
//                .findFirst()
//                .map(fieldError -> String.format("'%s' 항목은 정수 리스트여야 합니다.", fieldError.getField()))
//                .orElse("요청 값이 올바르지 않습니다.");

        return ResponseEntity.badRequest().body(
                createErrorResponse(
                        ErrorCode.BAD_REQUEST.getCode(),
                        ErrorCode.BAD_REQUEST.getMessage(),
                        message
                )
        );
    }



    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("📛 타입 변환 실패: {} (value = {})", ex.getName(), ex.getValue());

        String fieldName = ex.getName();
        String message;

        if (ex.getRequiredType() == LocalDate.class || ex.getRequiredType() == LocalDateTime.class) {
            message = "날짜 형식이 올바르지 않습니다. (yyyy-MM-dd 또는 yyyy-MM-ddTHH:mm:ss 형식)";
        } else if (ex.getRequiredType() == Integer.class) {
            message = String.format("'%s'는 숫자여야 합니다.", fieldName);
        } else {
            message = "요청 값의 타입이 잘못되었습니다.";
        }

        return ResponseEntity.badRequest().body(
                createErrorResponse(
                        ErrorCode.BAD_REQUEST.getCode(),
                        message,
                        null
                )
        );
    }


    /**
     * Validation 예외 처리 (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("📛 Validation 예외 발생: {}", ex.getMessage());

        FieldError firstFieldError = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .orElse(null);

        String message;
        if (firstFieldError != null) {
            String field = firstFieldError.getField();
            Object rejectedValue = firstFieldError.getRejectedValue();
            String requiredType = extractRequiredTypeFromCodes(firstFieldError.getCodes());

            message = String.format(
                    "'%s' 항목은 %s 타입이어야 합니다. (입력값: %s)",
                    field,
                    requiredType != null ? requiredType : "올바른",
                    rejectedValue
            );
        } else {
            message = "요청 형식이 올바르지 않습니다.";
        }

        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getCode() / 100)
                .body(createErrorResponse(
                        ErrorCode.BAD_REQUEST.getCode(),
                        message,
                        message
                ));
    }

    private String extractRequiredTypeFromCodes(String[] codes) {
        if (codes == null) return null;

        for (String code : codes) {
            if (code.startsWith("typeMismatch.")) {
                if (code.contains("java.lang.Integer")) return "정수";
                if (code.contains("java.util.List")) return "리스트";
                if (code.contains("java.lang.String")) return "문자열";
                if (code.contains("java.time.LocalDate")) return "날짜 (yyyy-MM-dd)";
                if (code.contains("java.time.LocalDateTime")) return "시간 (yyyy-MM-dd HH:mm:ss)";
            }
        }
        return null;
    }

    /**
     * 모든 예외 (Exception) 처리 (서버 내부 오류)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalExceptions(Exception ex) {
        log.error("📛 Unhandled 예외 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500)
                .body(createErrorResponse(
                        ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                        null
                ));
    }

    /**
     * 공통 에러 응답 생성기
     * @param errorCode 에러 코드
     * @param message 에러 메세지
     * @param details 에러 상세 내용
     * @return ErrorResponse 에러 응답
     */
    private ErrorResponse createErrorResponse(int errorCode, String message, String details) {
        return new ErrorResponse(errorCode, message, LocalDateTime.now().toString(), details);
    }

}

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
        log.error("HarusepakException 발생: {}", e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getCode() / 100)
                .body(createErrorResponse(
                        e.getErrorCode().getCode(),
                        e.getErrorCode().getMessage(),
                        e.getDetails())
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
        log.error("Validation 예외 발생: {}", ex.getMessage());

        String firstErrorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("잘못된 요청입니다.");

        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getCode()/ 100)
                .body(createErrorResponse(
                        ErrorCode.BAD_REQUEST.getCode(),
                        firstErrorMessage,
                        firstErrorMessage
                        )
                );
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ValidErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        log.error("Validation 예외 발생: {}", ex.getMessage());
//
//        return ResponseEntity.status(ErrorCode.MISSING_REQUIRED_FIELDS.getCode() / 100)
//                .body(new ValidErrorResponse(
//                        ErrorCode.MISSING_REQUIRED_FIELDS.getCode(),
//                        ErrorCode.MISSING_REQUIRED_FIELDS.getMessage(),
//                        LocalDateTime.now(),
//                        ex.getBindingResult().getFieldErrors().stream()
//                                .map(fieldError -> {
//                                    return new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
//                                }).toList()
//                ));
//    }

    /**
     * 모든 예외 (Exception) 처리 (서버 내부 오류)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalExceptions(Exception ex) {
        log.error("Unhandled 예외 발생: {}", ex.getMessage(), ex);
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

package com.haruspeak.api.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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


    /**
     * Validation 예외 처리 (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation 예외 발생: {}", ex.getMessage());

        return ResponseEntity.status(ErrorCode.MISSING_REQUIRED_FIELDS.getCode() / 100)
                .body(new ValidErrorResponse(
                        ErrorCode.MISSING_REQUIRED_FIELDS.getCode(),
                        ErrorCode.MISSING_REQUIRED_FIELDS.getMessage(),
                        LocalDateTime.now(),
                        ex.getBindingResult().getFieldErrors().stream()
                                .map(fieldError -> {
                                    return new FieldErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
                                }).toList()
                ));
    }

    /**
     * 모든 예외 (Exception) 처리 (서버 내부 오류)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalExceptions(Exception ex) {
        log.error("Unhandled 예외 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500)
                .body(createErrorResponse(500,ex.getMessage(),"Unexpected Error"));
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

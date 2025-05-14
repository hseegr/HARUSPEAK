package com.haruspeak.batch.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HaruspeakBatchException .class)
    public ResponseEntity<ErrorResponse> handleHaruspeakException(HaruspeakBatchException e) {
        log.error("⚠️ 예외 발생: {}", e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getCode() / 100)
                .body(createErrorResponse(
                        e.getErrorCode().getCode(),
                        e.getErrorCode().getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalExceptions(Exception ex) {
        log.error("Unhandled 예외 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500)
                .body(createErrorResponse(500,ex.getMessage()));
    }

    /**
     * 공통 에러 응답 생성기
     * @param errorCode 에러 코드
     * @param message 에러 메세지
     * @return ErrorResponse 에러 응답
     */
    private ErrorResponse createErrorResponse(int errorCode, String message) {
        return new ErrorResponse(errorCode, message, LocalDateTime.now().toString());
    }
}

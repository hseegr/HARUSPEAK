package com.haruspeak.api.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * HaruspeakException 예외 처리
     */
    @ExceptionHandler(HaruspeakException .class)
    public ResponseEntity<Map<String, Object>> handleHaruspeakException(HaruspeakException e) {
        log.error("HarusepakException 발생: {}", e.getErrorCode().getMessage());

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorCode", e.getErrorCode().getCode());  // 커스텀 에러 코드
        errorDetails.put("message", e.getErrorCode().getMessage()); // 기본 메시지
        errorDetails.put("timestamp", LocalDateTime.now());

        if(e.getDetails() != null) {
            log.error("HarusepakException details: {}", e.getDetails());
            errorDetails.put("details", e.getDetails()); // 추가 정보
        }

        return ResponseEntity.status(e.getErrorCode().getCode() / 100)
                .body(errorDetails);
    }




    /**
     * Validation 예외 처리 (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation 예외 발생: {}", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("errorCode", ErrorCode.MISSING_REQUIRED_FIELDS.getCode());
        response.put("timestamp", LocalDateTime.now());
        response.put("message", ErrorCode.MISSING_REQUIRED_FIELDS.getMessage());

        // 필드별 오류 메시지 추가
        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("field", fieldError.getField());
                    error.put("message", fieldError.getDefaultMessage());
                    return error;
                })
                .collect(Collectors.toList());
        response.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(ErrorCode.MISSING_REQUIRED_FIELDS.getCode() / 100)
                .body(response);
    }

    /**
     * 모든 예외 (Exception) 처리 (서버 내부 오류)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalExceptions(Exception ex) {
        log.error("Unhandled 예외 발생: {}", ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("errorCode", 500);
        response.put("details", "Unexpected Error");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(500)
                .body(response);
    }
}

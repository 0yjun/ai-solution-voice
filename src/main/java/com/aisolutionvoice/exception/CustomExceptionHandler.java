package com.aisolutionvoice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        // 1. 원본 에러 정보 추출
        ErrorCode originalErrorCode = ex.getErrorCode();

        // 2. 서버 로그 기록: 상세 정보 기록
        log.warn(
                "CustomException occurred: code={}, msg='{}', uri={}",
                originalErrorCode.getCode(),
                ex.getMessage(),
                request.getRequestURI(),
                ex  // 스택트레이스
        );

        // 4. 최종 응답 생성 및 반환
        return ResponseEntity
                .status(originalErrorCode.getHttpStatus())
                .body(new ErrorResponse(originalErrorCode.getCode(), originalErrorCode.getMessage())
                );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(Exception ex) {
        log.warn(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}

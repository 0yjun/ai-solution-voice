package com.aisolutionvoice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        ErrorResponse response = new ErrorResponse("DUPLICATE_RESOURCE", "이미 등록된 정보입니다.");
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ErrorCode.INTERNAL_COMMON_ERROR));
    }
}

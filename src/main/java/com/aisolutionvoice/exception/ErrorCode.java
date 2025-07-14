package com.aisolutionvoice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // JWT 관련

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED,"USR001", "비밀번호 혹은 아이디가 일치하지 않습니다"),
    DUPLICATE_USERNAME(HttpStatus.UNAUTHORIZED,"USR002", "이미 존재하는 아이디입니다."),

    // 권한 관련
    AUTH_COMMON_ERROR(HttpStatus.FORBIDDEN,"AUTH_000", "권한이 없습니다."),
    AUTH_NOT_FOUND(HttpStatus.UNAUTHORIZED,"AUTH_001","사용자 권한 정보를 찾을 수 없습니다."),
    INVALID_ROLE_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH_003","권한 형식이 올바르지 않습니다 (ROLE_ 접두사 필요)."),
    UNKNOWN_ROLE(HttpStatus.UNAUTHORIZED,      "AUTH_004", "정의되지 않은 권한입니다."),

    // 파라미터 관련
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VLD001", "%s 필드 오류: %s");

    private final HttpStatus httpStatus;
    private final String     code;
    private final String     message;
}

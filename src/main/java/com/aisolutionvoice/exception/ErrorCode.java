package com.aisolutionvoice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 권한 관련
    AUTH_NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "AUTH001", "로그인이 필요합니다."),
    AUTH_ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH002", "접근할 권한이 없습니다."),
    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED,"USR001", "비밀번호 혹은 아이디가 일치하지 않습니다"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT,"USR002", "이미 존재하는 아이디입니다."),

    // 권한 관련
    AUTH_COMMON_ERROR(HttpStatus.FORBIDDEN,"AUTH_000", "권한이 없습니다."),
    AUTH_NOT_FOUND(HttpStatus.UNAUTHORIZED,"AUTH_001","사용자 권한 정보를 찾을 수 없습니다."),
    INVALID_ROLE_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH_003","권한 형식이 올바르지 않습니다 (ROLE_ 접두사 필요)."),
    UNKNOWN_ROLE(HttpStatus.UNAUTHORIZED,      "AUTH_004", "정의되지 않은 권한입니다."),

    // 파라미터 관련
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VLD001", "%s 필드 오류: %s"),

    //파일처리관련 오류
    FILE_EMPTY_ERROR(HttpStatus.NOT_FOUND, "FILE001","저장할 파일이 비어있습니다"),
    FILE_SAVE_ERROR(HttpStatus.NOT_FOUND, "FILE002","저장할 파일이 비어있습니다"),
    INVALID_FILE_PATH(HttpStatus.NOT_FOUND, "FILE003","파일경로가 올바르지 않습니다"),

    DUPLICATE_POST_EXISTS(HttpStatus.CONFLICT, "POST001", "게시글이 중복되었습니다"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD001", "게시판을 찾을 수 없습니다."),
    BOARD_HAS_POSTS_CANNOT_MODIFY_SCRIPTS(HttpStatus.BAD_REQUEST, "BOARD002", "게시글이 존재하는 게시판의 스크립트는 수정할 수 없습니다."),
    HOTWORD_SCRIPT_NOT_FOUND(HttpStatus.NOT_FOUND, "HS001", "요청한 호출어 스크립트를 찾을 수 없습니다."),
    HOTWORD_SCRIPT_IS_ASSIGNED(HttpStatus.BAD_REQUEST, "HS002", "이미 게시판에 할당된 스크립트는 삭제할 수 없습니다."),
    // 서버오류
    INTERNAL_COMMON_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMN001","서버 내부 오류"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMN002","리소스를 찾을 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String     code;
    private final String     message;
}

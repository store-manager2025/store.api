package com.project.storemanager_api.exception;

// 1. ErrorCode에 등록,
// 2. 등록한 에러 관련 Exception객체 만듬 (ex> PostException)
// 3. GlobalExceptionHandler에서 override
// ex ) @ExceptionHandler(MemberException.class)
//        public ResponseEntity<ErrorResponse> handleMemberException(MemberException e, HttpServletRequest request)

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// API에서 나오는 여러가지 에러상황들을 상수로 표현
@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // 알 수 없는 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 오류입니다. 점검 후 조치하겠습니다."),

    // File 관련 오류
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 제한을 초과했습니다."),
    TOO_MANY_FILES(HttpStatus.BAD_REQUEST, "파일 개수가 제한을 초과했습니다."),

    // 회원관련 에러
    INVALID_SIGNUP_DATA(HttpStatus.BAD_REQUEST, "잘못된 회원가입 데이터입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

    // 인증 관련
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다."),


    ;

    private final HttpStatus status;
    private final String message;

}

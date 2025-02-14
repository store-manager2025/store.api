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
    EMPTY_EMAIL(HttpStatus.BAD_REQUEST, "email값이 누락되었습니다."),

    // 인증 관련
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다."),
    INVALID_ID(HttpStatus.NOT_FOUND, "유효하지 않은 ID입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다." ),

    // 입력값 검증
    EMPTY_DATA(HttpStatus.BAD_REQUEST, "이름, 비밀번호 중 한개의 값이라도 존재해야 합니다."),
    SAME_DATA(HttpStatus.BAD_REQUEST, "이전과 동일한 입력값입니다."),

    // store관련 에러
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 4자리의 숫자여야 합니다."),
    NOT_CORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다."),

    // ui관련 에러
    UI_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 ui 정보입니다.")

    // 주문 관련 에러

    ;

    private final HttpStatus status;
    private final String message;

}

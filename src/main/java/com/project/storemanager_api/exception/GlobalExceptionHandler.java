package com.project.storemanager_api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

// API에서 발생한 모든 에러들을 모아서 일괄 처리
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 알 수 없는 기타 등등 에러를 일괄 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);

        // 에러 응답 객체 생성
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .path(request.getRequestURI())
                .error(ErrorCode.INTERNAL_SERVER_ERROR.name())
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
                .build();

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(response);
    }


    // 회원관련
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(
            UserException e, HttpServletRequest request) {

        log.error("MemberException occurred: {}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(e.getErrorCode().getStatus().value())
                .error(e.getErrorCode().name())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(response);
    }

    // store관련
    @ExceptionHandler(StoreException.class)
    public ResponseEntity<ErrorResponse> handleStoreException(
            StoreException e, HttpServletRequest request) {

        log.error("StoreException occurred: {}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(e.getErrorCode().getStatus().value())
                .error(e.getErrorCode().name())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(response);
    }

    // menu관련
    @ExceptionHandler(MenuException.class)
    public ResponseEntity<ErrorResponse> handleMenuException(
            MenuException e, HttpServletRequest request) {

        log.error("MenuException occurred: {}", e.getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(e.getErrorCode().getStatus().value())
                .error(e.getErrorCode().name())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(response);
    }

}


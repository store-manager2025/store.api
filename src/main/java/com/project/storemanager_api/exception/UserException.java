package com.project.storemanager_api.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException{

    private final ErrorCode errorCode;

    public UserException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public UserException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

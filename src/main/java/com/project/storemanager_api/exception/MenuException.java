package com.project.storemanager_api.exception;

import lombok.Getter;

@Getter
public class MenuException extends RuntimeException{

    private final ErrorCode errorCode;

    public MenuException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public MenuException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

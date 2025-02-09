package com.project.storemanager_api.exception;

import lombok.Getter;

@Getter
public class UiException extends RuntimeException{

    private final ErrorCode errorCode;

    public UiException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public UiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

package com.project.storemanager_api.exception;

import lombok.Getter;

@Getter
public class EmpException extends RuntimeException {

    private final ErrorCode errorCode;

    public EmpException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public EmpException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

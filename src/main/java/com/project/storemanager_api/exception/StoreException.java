package com.project.storemanager_api.exception;

import lombok.Getter;

@Getter
public class StoreException extends RuntimeException{

    private final ErrorCode errorCode;

    public StoreException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public StoreException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}

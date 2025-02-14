package com.project.storemanager_api.exception;

import lombok.Getter;

@Getter
public class PlaceException extends RuntimeException {

    private final ErrorCode errorCode;

    public PlaceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public PlaceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}

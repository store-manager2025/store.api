package com.project.storemanager_api.exception;

import lombok.Getter;

@Getter
public class OrderException extends RuntimeException{

    private final ErrorCode errorCode;

    public OrderException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public OrderException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}

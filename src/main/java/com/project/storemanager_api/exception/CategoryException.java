package com.project.storemanager_api.exception;

import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException{

    private final ErrorCode errorCode;

    public CategoryException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CategoryException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}

package com.shop_api.backend.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * Base exception class following OOP principles All custom exceptions should extend this class
 */
@Getter
public abstract class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    protected BaseException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    protected BaseException(String message, Throwable cause, HttpStatus status, String errorCode) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }
}


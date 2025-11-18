package com.shop_api.backend.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when there's a conflict with existing resource
 */
public class ConflictException extends BaseException {
    private static final String ERROR_CODE = "CONFLICT";

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT, ERROR_CODE);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause, HttpStatus.CONFLICT, ERROR_CODE);
    }
}


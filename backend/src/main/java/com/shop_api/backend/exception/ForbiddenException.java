package com.shop_api.backend.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user doesn't have permission to access a resource
 */
public class ForbiddenException extends BaseException {
    private static final String ERROR_CODE = "FORBIDDEN";

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, ERROR_CODE);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause, HttpStatus.FORBIDDEN, ERROR_CODE);
    }
}


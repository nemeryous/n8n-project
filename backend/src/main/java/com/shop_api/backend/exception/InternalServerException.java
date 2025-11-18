package com.shop_api.backend.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for internal server errors
 */
public class InternalServerException extends BaseException {
    private static final String ERROR_CODE = "INTERNAL_SERVER_ERROR";

    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, ERROR_CODE);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR, ERROR_CODE);
    }
}


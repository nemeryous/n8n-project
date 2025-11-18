package com.shop_api.backend.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for bad request scenarios
 */
public class BadRequestException extends BaseException {
    private static final String ERROR_CODE = "BAD_REQUEST";

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ERROR_CODE);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, ERROR_CODE);
    }
}


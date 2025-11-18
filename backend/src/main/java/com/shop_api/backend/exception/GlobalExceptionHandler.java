package com.shop_api.backend.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.shop_api.backend.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler using @RestControllerAdvice Follows Single Responsibility Principle -
 * handles all exception responses Provides consistent error responses across the application
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle custom BaseException and its subclasses
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, WebRequest request) {
        log.error("Base exception occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(ex.getStatus().value()).error(ex.getStatus().getReasonPhrase())
                .errorCode(ex.getErrorCode()).message(ex.getMessage()).path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * Handle validation errors from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Validation error occurred: {}", ex.getMessage());

        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors().stream().map(this::mapFieldError).collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase()).errorCode("VALIDATION_ERROR")
                .message("Xác thực thất bại cho một hoặc nhiều trường")
                .path(getRequestPath(request)).validationErrors(validationErrors).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Spring Security authentication exceptions
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex,
            WebRequest request) {
        log.error("Authentication error: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase()).errorCode("AUTHENTICATION_ERROR")
                .message("Xác thực thất bại: " + ex.getMessage()).path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle bad credentials exception
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex,
            WebRequest request) {
        log.error("Bad credentials: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase()).errorCode("BAD_CREDENTIALS")
                .message("Email hoặc mật khẩu không hợp lệ").path(getRequestPath(request)).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handle Spring Security access denied exceptions
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
            WebRequest request) {
        log.error("Access denied: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value()).error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .errorCode("ACCESS_DENIED").message("Bạn không có quyền truy cập tài nguyên này")
                .path(getRequestPath(request)).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle method argument type mismatch
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.error("Type mismatch error: {}", ex.getMessage());

        String message = String.format(
                "Giá trị không hợp lệ '%s' cho tham số '%s'. Kiểu mong đợi: %s", ex.getValue(),
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName()
                        : "không xác định");

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase()).errorCode("TYPE_MISMATCH")
                .message(message).path(getRequestPath(request)).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle HTTP method not supported exceptions
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        log.error("HTTP method not supported: {} for path: {}", ex.getMethod(),
                getRequestPath(request));

        String supportedMethods =
                ex.getSupportedHttpMethods() != null
                        ? String.join(", ",
                                ex.getSupportedHttpMethods().stream()
                                        .map(httpMethod -> httpMethod.name()).toList())
                        : "không xác định";

        String message = String.format("Phương thức HTTP '%s' không được hỗ trợ cho đường dẫn này. "
                + "Các phương thức được hỗ trợ: %s", ex.getMethod(), supportedMethods);

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .errorCode("METHOD_NOT_ALLOWED").message(message).path(getRequestPath(request))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Handle 404 Not Found exceptions
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoHandlerFoundException ex,
            WebRequest request) {
        log.error("No handler found: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value()).error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .errorCode("ENDPOINT_NOT_FOUND").message("Đường dẫn yêu cầu không tồn tại")
                .path(getRequestPath(request)).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .errorCode("INTERNAL_ERROR")
                .message("Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.")
                .path(getRequestPath(request)).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Helper method to map FieldError to ValidationError Follows DRY principle
     */
    private ErrorResponse.ValidationError mapFieldError(FieldError fieldError) {
        return ErrorResponse.ValidationError.builder().field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(fieldError.getRejectedValue()).build();
    }

    /**
     * Helper method to extract request path Follows DRY principle
     */
    private String getRequestPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
            return servletRequest.getRequestURI();
        }
        return request.getDescription(false).replace("uri=", "");
    }
}


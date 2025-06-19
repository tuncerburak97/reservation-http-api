package com.reztech.reservation_http_api.core.exception;

/**
 * Exception for validation errors
 */
public class ValidationException extends BaseException {
    
    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    public ValidationException(String errorCode, String message, Object... messageArgs) {
        super(errorCode, message, messageArgs);
    }
    
    public ValidationException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    public ValidationException(String errorCode, String message, Throwable cause, Object... messageArgs) {
        super(errorCode, message, cause, messageArgs);
    }
} 
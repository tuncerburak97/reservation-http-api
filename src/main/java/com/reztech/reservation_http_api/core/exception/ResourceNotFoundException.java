package com.reztech.reservation_http_api.core.exception;

/**
 * Exception for resource not found scenarios
 */
public class ResourceNotFoundException extends BaseException {
    
    public ResourceNotFoundException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    public ResourceNotFoundException(String errorCode, String message, Object... messageArgs) {
        super(errorCode, message, messageArgs);
    }
    
    public ResourceNotFoundException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    public ResourceNotFoundException(String errorCode, String message, Throwable cause, Object... messageArgs) {
        super(errorCode, message, cause, messageArgs);
    }
} 
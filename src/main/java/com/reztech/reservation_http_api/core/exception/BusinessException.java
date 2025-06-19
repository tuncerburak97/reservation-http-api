package com.reztech.reservation_http_api.core.exception;

/**
 * Exception for business logic violations
 */
public class BusinessException extends BaseException {
    
    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    public BusinessException(String errorCode, String message, Object... messageArgs) {
        super(errorCode, message, messageArgs);
    }
    
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    public BusinessException(String errorCode, String message, Throwable cause, Object... messageArgs) {
        super(errorCode, message, cause, messageArgs);
    }
} 
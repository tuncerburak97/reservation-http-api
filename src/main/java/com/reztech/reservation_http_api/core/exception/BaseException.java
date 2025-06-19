package com.reztech.reservation_http_api.core.exception;

import lombok.Getter;

/**
 * Base exception class for all custom exceptions
 */
@Getter
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final Object[] messageArgs;
    
    protected BaseException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.messageArgs = new Object[0];
    }
    
    protected BaseException(String errorCode, String message, Object... messageArgs) {
        super(message);
        this.errorCode = errorCode;
        this.messageArgs = messageArgs;
    }
    
    protected BaseException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.messageArgs = new Object[0];
    }
    
    protected BaseException(String errorCode, String message, Throwable cause, Object... messageArgs) {
        super(message, cause);
        this.errorCode = errorCode;
        this.messageArgs = messageArgs;
    }
} 
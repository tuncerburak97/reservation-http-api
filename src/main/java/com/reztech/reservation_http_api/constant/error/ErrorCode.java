package com.reztech.reservation_http_api.constant.error;

/**
 * Error code constants
 */
public final class ErrorCode {
    
    private ErrorCode() {
        // Prevent instantiation
    }
    
    // User related errors
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";
    public static final String USER_EMAIL_ALREADY_EXISTS = "USER_EMAIL_ALREADY_EXISTS";
    public static final String USER_GSM_ALREADY_EXISTS = "USER_GSM_ALREADY_EXISTS";
    
    // Owner related errors
    public static final String OWNER_NOT_FOUND = "OWNER_NOT_FOUND";
    public static final String OWNER_ALREADY_EXISTS = "OWNER_ALREADY_EXISTS";
    public static final String OWNER_EMAIL_ALREADY_EXISTS = "OWNER_EMAIL_ALREADY_EXISTS";
    
    // Business related errors
    public static final String BUSINESS_NOT_FOUND = "BUSINESS_NOT_FOUND";
    public static final String BUSINESS_ALREADY_EXISTS = "BUSINESS_ALREADY_EXISTS";
    
    // Reservation related errors
    public static final String RESERVATION_NOT_FOUND = "RESERVATION_NOT_FOUND";
    public static final String RESERVATION_CONFLICT = "RESERVATION_CONFLICT";
    public static final String RESERVATION_PAST_DATE = "RESERVATION_PAST_DATE";
    
    // Validation errors
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String INVALID_EMAIL_FORMAT = "INVALID_EMAIL_FORMAT";
    public static final String INVALID_GSM_FORMAT = "INVALID_GSM_FORMAT";
    public static final String INVALID_DATE_FORMAT = "INVALID_DATE_FORMAT";
    public static final String FIELD_REQUIRED = "FIELD_REQUIRED";
    public static final String FIELD_TOO_LONG = "FIELD_TOO_LONG";
    public static final String FIELD_TOO_SHORT = "FIELD_TOO_SHORT";
    
    // General errors
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String INVALID_REQUEST = "INVALID_REQUEST";
} 
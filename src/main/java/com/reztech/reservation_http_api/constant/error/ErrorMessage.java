package com.reztech.reservation_http_api.constant.error;

/**
 * Error message constants
 */
public final class ErrorMessage {
    
    private ErrorMessage() {
        // Prevent instantiation
    }
    
    // User related messages
    public static final String USER_NOT_FOUND = "User not found with id: %s";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found with email: %s";
    public static final String USER_ALREADY_EXISTS = "User already exists with email: %s";
    public static final String USER_EMAIL_ALREADY_EXISTS = "User with this email already exists";
    public static final String USER_GSM_ALREADY_EXISTS = "User with this GSM number already exists";
    
    // Owner related messages
    public static final String OWNER_NOT_FOUND = "Owner not found with id: %s";
    public static final String OWNER_NOT_FOUND_BY_EMAIL = "Owner not found with email: %s";
    public static final String OWNER_ALREADY_EXISTS = "Owner already exists with email: %s";
    public static final String OWNER_EMAIL_ALREADY_EXISTS = "Owner with this email already exists";
    
    // Business related messages
    public static final String BUSINESS_NOT_FOUND = "Business not found with id: %s";
    public static final String BUSINESS_ALREADY_EXISTS = "Business already exists with name: %s";
    
    // Reservation related messages
    public static final String RESERVATION_NOT_FOUND = "Reservation not found with id: %s";
    public static final String RESERVATION_CONFLICT = "Reservation conflict for the selected time slot";
    public static final String RESERVATION_PAST_DATE = "Cannot create reservation for past date";
    
    // Validation messages
    public static final String VALIDATION_ERROR = "Validation failed";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String INVALID_GSM_FORMAT = "Invalid GSM number format";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";
    public static final String FIELD_REQUIRED = "This field is required";
    public static final String FIELD_TOO_LONG = "Field exceeds maximum length";
    public static final String FIELD_TOO_SHORT = "Field is below minimum length";
    
    // General messages
    public static final String INTERNAL_ERROR = "An internal error occurred";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String INVALID_REQUEST = "Invalid request";
} 
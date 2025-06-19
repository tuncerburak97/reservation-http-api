package com.reztech.reservation_http_api.model.enums;

/**
 * Type enumeration for business availability
 */
public enum AvailabilityType {
    WEEKLY_RECURRING,   // Recurring weekly schedule (e.g., every Monday)
    SPECIFIC_DATE,      // Specific date (e.g., 2024-01-15)
    DATE_RANGE         // Date range (e.g., 2024-01-15 to 2024-01-20)
} 
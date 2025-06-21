package com.reztech.reservation_http_api.model.enums;

/**
 * User type enumeration
 */
public enum UserType {
    CUSTOMER,       // Regular customers who book reservations
    EMPLOYEE,       // Business employees who can manage reservations
    BUSINESS_OWNER, // Business owners who can manage businesses and reservations
    ADMIN          // System administrators
} 
package com.reztech.reservation_http_api.model.enums;

/**
 * Business role enumeration
 * Defines roles that users can have within a business
 */
public enum BusinessRole {
    EMPLOYEE,   // Regular employee who can manage reservations
    PARTNER,    // Business partner with extended privileges
    MANAGER     // Manager with management privileges (between employee and owner)
} 
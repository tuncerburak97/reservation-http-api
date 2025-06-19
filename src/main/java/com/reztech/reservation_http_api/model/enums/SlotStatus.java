package com.reztech.reservation_http_api.model.enums;

/**
 * Status enumeration for time slots
 */
public enum SlotStatus {
    AVAILABLE,    // Slot is available for booking
    BOOKED,       // Slot is already booked
    BLOCKED       // Slot is blocked by business owner
} 
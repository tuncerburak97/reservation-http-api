package com.reztech.reservation_http_api.model.entity.main.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalTime;

/**
 * Business reservation settings entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reservation_settings")
public class ReservationSettings {
    
    @Id
    private String id;
    
    /**
     * Business ID this settings belongs to
     */
    private String businessId;
    
    /**
     * Default start time for reservations (e.g., 08:00)
     */
    @Builder.Default
    private LocalTime defaultStartTime = LocalTime.of(8, 0);
    
    /**
     * Default end time for reservations (e.g., 24:00)
     */
    @Builder.Default
    private LocalTime defaultEndTime = LocalTime.of(0, 0); // Midnight
    
    /**
     * Slot duration in minutes (default: 30)
     */
    @Builder.Default
    private Integer slotDurationMinutes = 30;
    
    /**
     * Maximum advance booking days (how many days in advance can customers book)
     */
    @Builder.Default
    private Integer maxAdvanceBookingDays = 30;
    
    /**
     * Minimum advance booking hours (minimum hours before appointment)
     */
    @Builder.Default
    private Integer minAdvanceBookingHours = 2;
    
    /**
     * Whether the business accepts reservations
     */
    @Builder.Default
    private Boolean acceptReservations = true;
    
    /**
     * Auto-confirm reservations or require manual approval
     */
    @Builder.Default
    private Boolean autoConfirm = true;
    
    private Instant createdAt;
    
    private Instant updatedAt;
} 
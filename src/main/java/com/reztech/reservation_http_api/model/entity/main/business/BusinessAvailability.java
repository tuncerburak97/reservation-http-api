package com.reztech.reservation_http_api.model.entity.main.business;

import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import com.reztech.reservation_http_api.model.enums.AvailabilityType;
import com.reztech.reservation_http_api.model.enums.ReservationDay;
import com.reztech.reservation_http_api.model.enums.SlotStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Business availability entity for managing time slots and blocked periods
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "business_availability")
public class BusinessAvailability {
    
    @Id
    private String id;
    
    /**
     * Business ID this availability belongs to
     */
    private String businessId;
    
    /**
     * Type of availability (weekly recurring, specific date, date range)
     */
    private AvailabilityType availabilityType;
    
    /**
     * Day of week (for WEEKLY_RECURRING type)
     */
    private ReservationDay dayOfWeek;
    
    /**
     * Specific date (for SPECIFIC_DATE type)
     */
    private LocalDate specificDate;
    
    /**
     * Start date (for DATE_RANGE type)
     */
    private LocalDate startDate;
    
    /**
     * End date (for DATE_RANGE type)
     */
    private LocalDate endDate;
    
    /**
     * Available time slots for this availability
     */
    private List<TimeSlot> availableSlots;
    
    /**
     * Blocked time slots for this availability
     */
    private List<TimeSlot> blockedSlots;
    
    /**
     * Overall status for this availability
     */
    @Builder.Default
    private SlotStatus status = SlotStatus.AVAILABLE;
    
    /**
     * Is this availability active
     */
    @Builder.Default
    private Boolean isActive = true;
    
    /**
     * Reason for blocking (if status is BLOCKED)
     */
    private String blockReason;
    
    private Instant createdAt;
    
    private Instant updatedAt;
} 
package com.reztech.reservation_http_api.model.api.request;

import com.reztech.reservation_http_api.model.entity.TimeSlot;
import com.reztech.reservation_http_api.model.enums.AvailabilityType;
import com.reztech.reservation_http_api.model.enums.ReservationDay;
import com.reztech.reservation_http_api.model.enums.SlotStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 * Request DTO for creating/updating business availability
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request model for creating/updating business availability rules")
public class CreateBusinessAvailabilityRequest {
    
    @NotBlank(message = "Business ID is required")
    @Schema(description = "ID of the business", example = "6507c123456789abcdef0003", required = true)
    private String businessId;
    
    @NotNull(message = "Availability type is required")
    @Schema(description = "Type of availability rule", example = "WEEKLY_RECURRING", required = true, allowableValues = {"WEEKLY_RECURRING", "SPECIFIC_DATE", "DATE_RANGE"})
    private AvailabilityType availabilityType;
    
    @Schema(description = "Day of week for WEEKLY_RECURRING type", example = "MONDAY", allowableValues = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"})
    private ReservationDay dayOfWeek;
    
    @Schema(description = "Specific date for SPECIFIC_DATE type", example = "2024-12-25", type = "string", format = "date")
    private LocalDate specificDate;
    
    @Schema(description = "Start date for DATE_RANGE type", example = "2024-12-20", type = "string", format = "date")
    private LocalDate startDate;
    
    @Schema(description = "End date for DATE_RANGE type", example = "2024-12-31", type = "string", format = "date")
    private LocalDate endDate;
    
    @Valid
    @Schema(description = "List of available time slots")
    private List<TimeSlot> availableSlots;
    
    @Valid
    @Schema(description = "List of blocked time slots")
    private List<TimeSlot> blockedSlots;
    
    @Schema(description = "Status of the availability", example = "AVAILABLE", allowableValues = {"AVAILABLE", "BOOKED", "BLOCKED"})
    private SlotStatus status;
    
    @Schema(description = "Whether this availability rule is active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Reason for blocking time slots", example = "Tatil")
    private String blockReason;
} 
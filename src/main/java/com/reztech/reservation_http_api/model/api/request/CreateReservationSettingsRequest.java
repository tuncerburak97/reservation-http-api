package com.reztech.reservation_http_api.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalTime;

/**
 * Request DTO for creating/updating reservation settings
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request model for creating/updating business reservation settings")
public class CreateReservationSettingsRequest {
    
    @NotBlank(message = "Business ID is required")
    @Schema(description = "ID of the business", example = "6507c123456789abcdef0003", required = true)
    private String businessId;
    
    @NotNull(message = "Default start time is required")
    @Schema(description = "Default daily start time for reservations", example = "08:00", required = true, type = "string", format = "time")
    private LocalTime defaultStartTime;
    
    @NotNull(message = "Default end time is required") 
    @Schema(description = "Default daily end time for reservations", example = "18:00", required = true, type = "string", format = "time")
    private LocalTime defaultEndTime;
    
    @Min(value = 15, message = "Slot duration must be at least 15 minutes")
    @Max(value = 120, message = "Slot duration must be at most 120 minutes")
    @Schema(description = "Duration of each reservation slot in minutes", example = "30", minimum = "15", maximum = "120")
    private Integer slotDurationMinutes;
    
    @Min(value = 1, message = "Max advance booking days must be at least 1")
    @Max(value = 365, message = "Max advance booking days must be at most 365")
    @Schema(description = "Maximum days in advance that customers can make reservations", example = "30", minimum = "1", maximum = "365")
    private Integer maxAdvanceBookingDays;
    
    @Min(value = 0, message = "Min advance booking hours must be at least 0")
    @Max(value = 72, message = "Min advance booking hours must be at most 72")
    @Schema(description = "Minimum hours in advance required for making reservations", example = "2", minimum = "0", maximum = "72")
    private Integer minAdvanceBookingHours;
    
    @Schema(description = "Whether the business accepts reservations", example = "true")
    private Boolean acceptReservations;
    
    @Schema(description = "Whether reservations are automatically confirmed without manual approval", example = "true")
    private Boolean autoConfirm;
} 
package com.reztech.reservation_http_api.model.api.request;

import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * Request DTO for creating reservations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request model for creating a new reservation")
public class CreateReservationRequest {
    
    @NotBlank(message = "User ID is required")
    @Schema(description = "ID of the user making the reservation", example = "6507c123456789abcdef0002", required = true)
    private String userId;
    
    @NotBlank(message = "Business ID is required")
    @Schema(description = "ID of the business where reservation is made", example = "6507c123456789abcdef0003", required = true)
    private String businessId;
    
    @NotNull(message = "Reservation date is required")
    @Schema(description = "Date of the reservation", example = "2024-12-25", required = true, type = "string", format = "date")
    private LocalDate reservationDate;
    
    @NotNull(message = "Time slot is required")
    @Valid
    @Schema(description = "Time slot for the reservation", required = true)
    private TimeSlot timeSlot;
    
    @Schema(description = "User ID of the assigned employee for this reservation", example = "6507c123456789abcdef0004")
    private String assignedEmployeeUserId;
    
    @Schema(description = "Additional notes for the reservation", example = "Saç kesimi istiyorum")
    private String notes;
} 
package com.reztech.reservation_http_api.model.api.response;

import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.model.enums.SlotStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Response DTO for reservations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response model for reservation information")
public class ReservationResponse {
    
    @Schema(description = "Unique identifier of the reservation", example = "6507c123456789abcdef0004")
    private String id;
    
    @Schema(description = "User who made the reservation")
    private User user;
    
    @Schema(description = "Business where the reservation is made")
    private Business business;
    
    @Schema(description = "Date of the reservation", example = "2024-12-25")
    private LocalDate reservationDate;
    
    @Schema(description = "Time slot for the reservation")
    private TimeSlot timeSlot;
    
    @Schema(description = "User ID of the assigned employee for this reservation", example = "6507c123456789abcdef0004")
    private String assignedEmployeeUserId;
    
    @Schema(description = "Status of the reservation slot", example = "BOOKED")
    private SlotStatus status;
    
    @Schema(description = "Whether the reservation is confirmed", example = "false")
    private Boolean isConfirmed;
    
    @Schema(description = "Whether the reservation is cancelled", example = "false")
    private Boolean isCancelled;
    
    @Schema(description = "Cancellation reason if cancelled", example = "Customer requested cancellation")
    private String cancellationReason;
    
    @Schema(description = "Additional notes for the reservation", example = "Saç kesimi istiyorum")
    private String notes;
    
    @Schema(description = "Date and time when the reservation was created", example = "2024-12-20T14:30:00Z")
    private Instant createdAt;
    
    @Schema(description = "Date and time when the reservation was last updated", example = "2024-12-20T14:30:00Z")
    private Instant updatedAt;
} 
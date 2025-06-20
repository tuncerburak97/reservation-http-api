package com.reztech.reservation_http_api.model.api.response;

import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;

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
    
    @Schema(description = "Date and time of the reservation", example = "2024-12-25T10:00:00Z")
    private Instant reservationDate;
    
    @Schema(description = "Date and time when the reservation was created", example = "2024-12-20T14:30:00Z")
    private Instant createdAt;
    
    @Schema(description = "Date and time when the reservation was last updated", example = "2024-12-20T14:30:00Z")
    private Instant updatedAt;
} 
package com.reztech.reservation_http_api.model.api.response;

import com.reztech.reservation_http_api.model.entity.embedded.TimeSlot;
import com.reztech.reservation_http_api.model.enums.SlotStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 * Response DTO for available time slots
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response model for available time slots information")
public class AvailableSlotResponse {
    
    @Schema(description = "ID of the business", example = "6507c123456789abcdef0003")
    private String businessId;
    
    @Schema(description = "Date for which slots are queried", example = "2024-12-25", type = "string", format = "date")
    private LocalDate date;
    
    @Schema(description = "List of available time slots")
    private List<SlotInfo> availableSlots;
    
    @Schema(description = "List of blocked time slots")
    private List<SlotInfo> blockedSlots;
    
    @Schema(description = "List of booked time slots")
    private List<SlotInfo> bookedSlots;
    
    /**
     * Slot information with status
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Information about a specific time slot")
    public static class SlotInfo {
        @Schema(description = "Time slot details")
        private TimeSlot timeSlot;
        
        @Schema(description = "Current status of the slot", example = "AVAILABLE", allowableValues = {"AVAILABLE", "BOOKED", "BLOCKED"})
        private SlotStatus status;
        
        @Schema(description = "Reason if slot is blocked or booked", example = "Çalışan izinde")
        private String reason;
        
        @Schema(description = "Whether this slot can be booked", example = "true")
        private Boolean isBookable;
    }
} 
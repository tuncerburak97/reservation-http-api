package com.reztech.reservation_http_api.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalTime;

/**
 * Time slot entity representing 30-minute intervals
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Time slot model representing a reservation time interval")
public class TimeSlot {
    
    @Schema(description = "Start time of the slot", example = "09:00", type = "string", format = "time")
    private LocalTime startTime;
    
    @Schema(description = "End time of the slot", example = "09:30", type = "string", format = "time")
    private LocalTime endTime;
    
    /**
     * Create a 30-minute time slot starting from the given time
     * @param startTime start time
     * @return TimeSlot with 30-minute duration
     */
    public static TimeSlot of(LocalTime startTime) {
        return TimeSlot.builder()
                .startTime(startTime)
                .endTime(startTime.plusMinutes(30))
                .build();
    }
    
    /**
     * Create a time slot with custom duration
     * @param startTime start time
     * @param endTime end time
     * @return TimeSlot with custom duration
     */
    public static TimeSlot of(LocalTime startTime, LocalTime endTime) {
        return TimeSlot.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
    
    /**
     * Check if this slot overlaps with another slot
     * @param other another time slot
     * @return true if overlaps
     */
    public boolean overlaps(TimeSlot other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }
    
    /**
     * Get slot as string representation (e.g., "08:00-08:30")
     * @return formatted slot string
     */
    public String getSlotString() {
        return String.format("%s-%s", startTime.toString(), endTime.toString());
    }
} 
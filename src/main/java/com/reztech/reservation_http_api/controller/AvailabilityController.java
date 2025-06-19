package com.reztech.reservation_http_api.controller;

import com.reztech.reservation_http_api.model.api.response.AvailableSlotResponse;
import com.reztech.reservation_http_api.service.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Availability operations
 */
@Slf4j
@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
@Tag(name = "Availability", description = "Availability checking and time slot operations for businesses")
public class AvailabilityController {
    
    private final AvailabilityService availabilityService;
    
    /**
     * Get available slots for a specific business and date
     * @param businessId Business ID
     * @param date Target date (format: yyyy-MM-dd)
     * @return Available slot response
     */
    @GetMapping("/business/{businessId}/date/{date}")
    @Operation(
        summary = "Get available slots for a specific date",
        description = "Retrieves all available, booked, and blocked time slots for a business on a specific date"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Available slots retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AvailableSlotResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid date format"
        )
    })
    public ResponseEntity<AvailableSlotResponse> getAvailableSlots(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId,
        @Parameter(description = "Target date in yyyy-MM-dd format", required = true, example = "2024-12-25")
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("Get available slots request received for business: {} on date: {}", businessId, date);
        
        AvailableSlotResponse response = availabilityService.getAvailableSlots(businessId, date);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get available slots for a date range
     * @param businessId Business ID
     * @param startDate Start date (format: yyyy-MM-dd)
     * @param endDate End date (format: yyyy-MM-dd)
     * @return List of available slot responses
     */
    @GetMapping("/business/{businessId}/range")
    @Operation(
        summary = "Get available slots for a date range",
        description = "Retrieves available slots for a business within a specified date range"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Available slots for range retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AvailableSlotResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid date format or range"
        )
    })
    public ResponseEntity<List<AvailableSlotResponse>> getAvailableSlotsForRange(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId,
        @Parameter(description = "Start date in yyyy-MM-dd format", required = true, example = "2024-12-20")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @Parameter(description = "End date in yyyy-MM-dd format", required = true, example = "2024-12-31")
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        log.info("Get available slots for range request received for business: {} from {} to {}", 
                businessId, startDate, endDate);
        
        List<AvailableSlotResponse> responses = availabilityService
                .getAvailableSlotsForRange(businessId, startDate, endDate);
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Get available slots for next week
     * @param businessId Business ID
     * @return List of available slot responses for next 7 days
     */
    @GetMapping("/business/{businessId}/week")
    @Operation(
        summary = "Get available slots for next week",
        description = "Retrieves available slots for a business for the next 7 days starting from today"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Available slots for next week retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AvailableSlotResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<List<AvailableSlotResponse>> getAvailableSlotsForWeek(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Get available slots for next week request received for business: {}", businessId);
        
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        
        List<AvailableSlotResponse> responses = availabilityService
                .getAvailableSlotsForRange(businessId, today, nextWeek);
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Get available slots for next month
     * @param businessId Business ID
     * @return List of available slot responses for next 30 days
     */
    @GetMapping("/business/{businessId}/month")
    @Operation(
        summary = "Get available slots for next month",
        description = "Retrieves available slots for a business for the next 30 days starting from today"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Available slots for next month retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AvailableSlotResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<List<AvailableSlotResponse>> getAvailableSlotsForMonth(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Get available slots for next month request received for business: {}", businessId);
        
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusDays(30);
        
        List<AvailableSlotResponse> responses = availabilityService
                .getAvailableSlotsForRange(businessId, today, nextMonth);
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Get available slots for today
     * @param businessId Business ID
     * @return Available slot response for today
     */
    @GetMapping("/business/{businessId}/today")
    @Operation(
        summary = "Get available slots for today",
        description = "Retrieves available slots for a business for today's date"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Available slots for today retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AvailableSlotResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<AvailableSlotResponse> getAvailableSlotsForToday(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Get available slots for today request received for business: {}", businessId);
        
        LocalDate today = LocalDate.now();
        AvailableSlotResponse response = availabilityService.getAvailableSlots(businessId, today);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get available slots for tomorrow
     * @param businessId Business ID
     * @return Available slot response for tomorrow
     */
    @GetMapping("/business/{businessId}/tomorrow")
    @Operation(
        summary = "Get available slots for tomorrow",
        description = "Retrieves available slots for a business for tomorrow's date"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Available slots for tomorrow retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AvailableSlotResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<AvailableSlotResponse> getAvailableSlotsForTomorrow(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Get available slots for tomorrow request received for business: {}", businessId);
        
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        AvailableSlotResponse response = availabilityService.getAvailableSlots(businessId, tomorrow);
        return ResponseEntity.ok(response);
    }
} 
package com.reztech.reservation_http_api.controller;

import com.reztech.reservation_http_api.model.api.request.CreateReservationSettingsRequest;
import com.reztech.reservation_http_api.model.entity.ReservationSettings;
import com.reztech.reservation_http_api.service.ReservationSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Reservation Settings operations
 */
@Slf4j
@RestController
@RequestMapping("/api/reservation-settings")
@RequiredArgsConstructor
@Tag(name = "Reservation Settings", description = "Business reservation configuration operations including time slots, booking rules, and business hours")
public class ReservationSettingsController {
    
    private final ReservationSettingsService reservationSettingsService;
    
    /**
     * Create or update reservation settings
     * @param request Create reservation settings request
     * @return Created/Updated reservation settings
     */
    @PostMapping
    @Operation(
        summary = "Create or update reservation settings",
        description = "Creates new reservation settings or updates existing settings for a business. " +
                     "These settings control time slots, booking rules, and business hours."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Reservation settings created/updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationSettings.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid settings data provided"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<ReservationSettings> createOrUpdateSettings(
        @Parameter(description = "Reservation settings creation request", required = true)
        @Valid @RequestBody CreateReservationSettingsRequest request
    ) {
        log.info("Create/update reservation settings request received for business: {}", 
                request.getBusinessId());
        
        ReservationSettings settings = reservationSettingsService.createOrUpdateSettings(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(settings);
    }
    
    /**
     * Get reservation settings by business ID
     * @param businessId Business ID
     * @return Reservation settings
     */
    @GetMapping("/business/{businessId}")
    @Operation(
        summary = "Get reservation settings by business ID",
        description = "Retrieves the reservation settings for a specific business"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation settings retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationSettings.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business or settings not found"
        )
    })
    public ResponseEntity<ReservationSettings> getSettingsByBusinessId(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Get reservation settings request received for business: {}", businessId);
        
        ReservationSettings settings = reservationSettingsService.getSettingsByBusinessId(businessId);
        return ResponseEntity.ok(settings);
    }
    
    /**
     * Get all reservation settings
     * @return List of all reservation settings
     */
    @GetMapping
    @Operation(
        summary = "Get all reservation settings",
        description = "Retrieves a list of all reservation settings for all businesses"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "All reservation settings retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationSettings.class)
            )
        )
    })  
    public ResponseEntity<List<ReservationSettings>> getAllSettings() {
        log.info("Get all reservation settings request received");
        
        List<ReservationSettings> settings = reservationSettingsService.getAllSettings();
        return ResponseEntity.ok(settings);
    }
    
    /**
     * Delete reservation settings by business ID
     * @param businessId Business ID
     * @return No content response
     */
    @DeleteMapping("/business/{businessId}")
    @Operation(
        summary = "Delete reservation settings by business ID",
        description = "Removes the reservation settings for a specific business"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Reservation settings deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business or settings not found"
        )
    })
    public ResponseEntity<Void> deleteSettingsByBusinessId(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Delete reservation settings request received for business: {}", businessId);
        
        reservationSettingsService.deleteSettingsByBusinessId(businessId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Get or create default settings for a business
     * @param businessId Business ID
     * @return Default reservation settings
     */
    @GetMapping("/business/{businessId}/default")
    @Operation(
        summary = "Get or create default settings",
        description = "Retrieves existing reservation settings for a business or creates default settings if none exist"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Default reservation settings retrieved/created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationSettings.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<ReservationSettings> getOrCreateDefaultSettings(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Get or create default settings request received for business: {}", businessId);
        
        ReservationSettings settings = reservationSettingsService.getOrCreateDefaultSettings(businessId);
        return ResponseEntity.ok(settings);
    }
} 
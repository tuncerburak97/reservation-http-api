package com.reztech.reservation_http_api.controller;

import com.reztech.reservation_http_api.model.api.request.CreateReservationRequest;
import com.reztech.reservation_http_api.model.api.response.ReservationResponse;
import com.reztech.reservation_http_api.service.ReservationService;
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
 * REST Controller for Reservation operations
 */
@Slf4j
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Reservation management operations for creating, updating, and querying reservations")
public class ReservationController {
    
    private final ReservationService reservationService;
    
    /**
     * Create a new reservation
     * @param request Create reservation request
     * @return Created reservation response
     */
    @PostMapping
    @Operation(
        summary = "Create a new reservation",
        description = "Creates a new reservation for the specified user at the given business and time slot. " +
                     "The system will check availability and prevent double-booking."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Reservation created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid reservation data or time slot not available"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User or business not found"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Time slot already booked"
        )
    })
    public ResponseEntity<ReservationResponse> createReservation(
        @Parameter(description = "Reservation creation request", required = true)
        @Valid @RequestBody CreateReservationRequest request
    ) {
        log.info("Creating reservation request received");
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Update an existing reservation
     * @param id Reservation ID
     * @param request Update reservation request
     * @return Updated reservation response
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing reservation",
        description = "Updates the reservation details including date, time slot, or notes"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid reservation data"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reservation not found"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "New time slot already booked"
        )
    })
    public ResponseEntity<ReservationResponse> updateReservation(
        @Parameter(description = "Reservation ID", required = true, example = "6507c123456789abcdef0004")
        @PathVariable String id,
        @Parameter(description = "Reservation update request", required = true)
        @Valid @RequestBody CreateReservationRequest request
    ) {
        log.info("Update reservation request received for id: {}", id);
        ReservationResponse response = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all reservations
     * @return List of all reservations
     */
    @GetMapping
    @Operation(
        summary = "Get all reservations",
        description = "Retrieves a list of all reservations in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservations retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        )
    })
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        log.info("Get all reservations request received");
        List<ReservationResponse> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Find reservation by ID
     * @param id Reservation ID
     * @return Reservation response
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Find reservation by ID",
        description = "Retrieves a specific reservation by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservation found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reservation not found"
        )
    })
    public ResponseEntity<ReservationResponse> findById(
        @Parameter(description = "Reservation ID", required = true, example = "6507c123456789abcdef0004")
        @PathVariable String id
    ) {
        log.info("Find reservation by id request received for id: {}", id);
        ReservationResponse response = reservationService.findById(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Find reservations by business ID
     * @param businessId Business ID
     * @return List of reservations for the business
     */
    @GetMapping("/business/{businessId}")
    @Operation(
        summary = "Find reservations by business ID",
        description = "Retrieves all reservations for a specific business"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservations found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<List<ReservationResponse>> findByBusinessId(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Find reservations by business id request received for businessId: {}", businessId);
        List<ReservationResponse> reservations = reservationService.findByBusinessId(businessId);
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Find reservations by user ID
     * @param userId User ID
     * @return List of reservations for the user
     */
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Find reservations by user ID",
        description = "Retrieves all reservations made by a specific user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reservations found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReservationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<List<ReservationResponse>> findByUserId(
        @Parameter(description = "User ID", required = true, example = "6507c123456789abcdef0002")
        @PathVariable String userId
    ) {
        log.info("Find reservations by user id request received for userId: {}", userId);
        List<ReservationResponse> reservations = reservationService.findByUserId(userId);
        return ResponseEntity.ok(reservations);
    }
    
    /**
     * Delete reservation by ID
     * @param id Reservation ID
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete reservation by ID",
        description = "Cancels and removes a specific reservation by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Reservation deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reservation not found"
        )
    })
    public ResponseEntity<Void> deleteReservation(
        @Parameter(description = "Reservation ID", required = true, example = "6507c123456789abcdef0004")
        @PathVariable String id
    ) {
        log.info("Delete reservation request received for id: {}", id);
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
} 
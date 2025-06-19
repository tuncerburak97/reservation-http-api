package com.reztech.reservation_http_api.controller;

import com.reztech.reservation_http_api.model.api.request.CreateBusinessRequest;
import com.reztech.reservation_http_api.model.entity.Business;
import com.reztech.reservation_http_api.service.BusinessService;
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
 * REST Controller for Business operations
 */
@Slf4j
@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
@Tag(name = "Businesses", description = "Business management operations including create, read, update, and delete operations")
public class BusinessController {
    
    private final BusinessService businessService;
    
    /**
     * Create a new business
     * @param request Create business request
     * @return Created business
     */
    @PostMapping
    @Operation(
        summary = "Create a new business",
        description = "Creates a new business with the provided information. The business will be available for reservations."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Business created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid business data provided"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Owner not found"
        )
    })
    public ResponseEntity<Business> createBusiness(
        @Parameter(description = "Business creation request", required = true)
        @Valid @RequestBody CreateBusinessRequest request
    ) {
        log.info("Creating business request received");
        Business business = businessService.createBusiness(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }
    
    /**
     * Update an existing business
     * @param id Business ID
     * @param request Update business request
     * @return Updated business
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing business",
        description = "Updates the business information with the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Business updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid business data provided"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<Business> updateBusiness(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String id,
        @Parameter(description = "Business update request", required = true)
        @Valid @RequestBody CreateBusinessRequest request
    ) {
        log.info("Update business request received for id: {}", id);
        Business business = businessService.updateBusiness(id, request);
        return ResponseEntity.ok(business);
    }
    
    /**
     * Get all businesses
     * @return List of all businesses
     */
    @GetMapping
    @Operation(
        summary = "Get all businesses",
        description = "Retrieves a list of all registered businesses in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Businesses retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        )
    })
    public ResponseEntity<List<Business>> getAllBusinesses() {
        log.info("Get all businesses request received");
        List<Business> businesses = businessService.getAllBusinesses();
        return ResponseEntity.ok(businesses);
    }
    
    /**
     * Find business by ID
     * @param id Business ID
     * @return Business
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Find business by ID",
        description = "Retrieves a specific business by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Business found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<Business> findById(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String id
    ) {
        log.info("Find business by id request received for id: {}", id);
        Business business = businessService.findById(id);
        return ResponseEntity.ok(business);
    }
    
    /**
     * Find businesses by owner ID
     * @param ownerId Owner ID
     * @return List of businesses
     */
    @GetMapping("/owner/{ownerId}")
    @Operation(
        summary = "Find businesses by owner ID",
        description = "Retrieves all businesses owned by the specified owner"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Businesses found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Owner not found"
        )
    })
    public ResponseEntity<List<Business>> findByOwnerId(
        @Parameter(description = "Owner ID", required = true, example = "6507c123456789abcdef0001")
        @PathVariable String ownerId
    ) {
        log.info("Find businesses by owner id request received for ownerId: {}", ownerId);
        List<Business> businesses = businessService.findByOwnerId(ownerId);
        return ResponseEntity.ok(businesses);
    }
    
    /**
     * Search businesses by name
     * @param name Business name
     * @return List of businesses
     */
    @GetMapping("/search")
    @Operation(
        summary = "Search businesses by name",
        description = "Search for businesses by name using partial matching"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Businesses found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        )
    })
    public ResponseEntity<List<Business>> searchByName(
        @Parameter(description = "Business name to search", required = true, example = "Güzellik")
        @RequestParam String name
    ) {
        log.info("Search businesses by name request received for name: {}", name);
        List<Business> businesses = businessService.searchByName(name);
        return ResponseEntity.ok(businesses);
    }
    
    /**
     * Delete business by ID
     * @param id Business ID
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete business by ID",
        description = "Deletes a specific business by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Business deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<Void> deleteBusiness(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String id
    ) {
        log.info("Delete business request received for id: {}", id);
        businessService.deleteBusiness(id);
        return ResponseEntity.noContent().build();
    }
} 
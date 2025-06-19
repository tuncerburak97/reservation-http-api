package com.reztech.reservation_http_api.controller;

import com.reztech.reservation_http_api.model.api.request.CreateOwnerRequest;
import com.reztech.reservation_http_api.model.entity.Owner;
import com.reztech.reservation_http_api.model.enums.OwnerType;
import com.reztech.reservation_http_api.service.OwnerService;
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
 * REST Controller for Owner operations
 */
@Slf4j
@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
@Tag(name = "Owners", description = "Business owners management operations including create, read, update, and delete operations")
public class OwnerController {
    
    private final OwnerService ownerService;
    
    /**
     * Create a new owner
     * @param request Create owner request
     * @return Created owner
     */
    @PostMapping
    @Operation(
        summary = "Create a new business owner",
        description = "Creates a new business owner who can manage businesses and reservations"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Owner created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Owner.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid owner data provided"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Owner with this email already exists"
        )
    })
    public ResponseEntity<Owner> createOwner(
        @Parameter(description = "Owner creation request", required = true)
        @Valid @RequestBody CreateOwnerRequest request
    ) {
        log.info("Creating owner request received");
        Owner owner = ownerService.createOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(owner);
    }
    
    /**
     * Update an existing owner
     * @param id Owner ID
     * @param request Update owner request
     * @return Updated owner
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing owner",
        description = "Updates the owner information with the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Owner updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Owner.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid owner data provided"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Owner not found"
        )
    })
    public ResponseEntity<Owner> updateOwner(
        @Parameter(description = "Owner ID", required = true, example = "6507c123456789abcdef0001")
        @PathVariable String id,
        @Parameter(description = "Owner update request", required = true)
        @Valid @RequestBody CreateOwnerRequest request
    ) {
        log.info("Update owner request received for id: {}", id);
        Owner owner = ownerService.updateOwner(id, request);
        return ResponseEntity.ok(owner);
    }
    
    /**
     * Get all owners
     * @return List of all owners
     */
    @GetMapping
    @Operation(
        summary = "Get all owners",
        description = "Retrieves a list of all registered business owners in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Owners retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Owner.class)
            )
        )
    })
    public ResponseEntity<List<Owner>> getAllOwners() {
        log.info("Get all owners request received");
        List<Owner> owners = ownerService.getAllOwners();
        return ResponseEntity.ok(owners);
    }
    
    /**
     * Find owner by ID
     * @param id Owner ID
     * @return Owner
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Find owner by ID",
        description = "Retrieves a specific owner by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Owner found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Owner.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Owner not found"
        )
    })
    public ResponseEntity<Owner> findById(
        @Parameter(description = "Owner ID", required = true, example = "6507c123456789abcdef0001")
        @PathVariable String id
    ) {
        log.info("Find owner by id request received for id: {}", id);
        Owner owner = ownerService.findById(id);
        return ResponseEntity.ok(owner);
    }
    
    /**
     * Find owner by email
     * @param email Owner email
     * @return Owner
     */
    @GetMapping("/email/{email}")
    @Operation(
        summary = "Find owner by email",
        description = "Retrieves a specific owner by their email address"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Owner found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Owner.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Owner not found"
        )
    })
    public ResponseEntity<Owner> findByEmail(
        @Parameter(description = "Owner email address", required = true, example = "mehmet.ozkan@business.com")
        @PathVariable String email
    ) {
        log.info("Find owner by email request received for email: {}", email);
        Owner owner = ownerService.findByEmail(email);
        return ResponseEntity.ok(owner);
    }
    
    /**
     * Find owners by type
     * @param ownerType Owner type
     * @return List of owners
     */
    @GetMapping("/type/{ownerType}")
    @Operation(
        summary = "Find owners by type",
        description = "Retrieves all owners of a specific type (ADMIN or NORMAL_USER)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Owners found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Owner.class)
            )
        )
    })
    public ResponseEntity<List<Owner>> findByOwnerType(
        @Parameter(description = "Owner type", required = true, example = "ADMIN")
        @PathVariable OwnerType ownerType
    ) {
        log.info("Find owners by type request received for type: {}", ownerType);
        List<Owner> owners = ownerService.findByOwnerType(ownerType);
        return ResponseEntity.ok(owners);
    }
    
    /**
     * Delete owner by ID
     * @param id Owner ID
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete owner by ID",
        description = "Deletes a specific owner by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Owner deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Owner not found"
        )
    })
    public ResponseEntity<Void> deleteOwner(
        @Parameter(description = "Owner ID", required = true, example = "6507c123456789abcdef0001")
        @PathVariable String id
    ) {
        log.info("Delete owner request received for id: {}", id);
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }
} 
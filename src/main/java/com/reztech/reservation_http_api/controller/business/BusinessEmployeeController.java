package com.reztech.reservation_http_api.controller.business;

import com.reztech.reservation_http_api.model.api.request.AddBusinessEmployeeRequest;
import com.reztech.reservation_http_api.model.api.request.UpdateBusinessEmployeeRequest;
import com.reztech.reservation_http_api.model.api.response.BusinessEmployeeResponse;
import com.reztech.reservation_http_api.model.entity.main.business.Business;
import com.reztech.reservation_http_api.service.business.BusinessEmployeeService;
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
 * REST Controller for Business Employee operations
 */
@Slf4j
@RestController
@RequestMapping("/api/businesses/{businessId}/employees")
@RequiredArgsConstructor
@Tag(name = "Business Employees", description = "Business employee management operations including add, update, remove and list employees")
public class BusinessEmployeeController {
    
    private final BusinessEmployeeService businessEmployeeService;
    
    /**
     * Add an employee to a business
     * @param businessId Business ID
     * @param request Add employee request
     * @return Updated business
     */
    @PostMapping
    @Operation(
        summary = "Add an employee to a business",
        description = "Adds a user as an employee or partner to the specified business. The user must not be a customer type."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Employee added successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data or user cannot be added as employee"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business or user not found"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "User is already an employee of this business"
        )
    })
    public ResponseEntity<Business> addEmployee(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId,
        @Parameter(description = "Add employee request", required = true)
        @Valid @RequestBody AddBusinessEmployeeRequest request
    ) {
        log.info("Add employee request received for business: {}", businessId);
        Business business = businessEmployeeService.addEmployee(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(business);
    }
    
    /**
     * Update a business employee
     * @param businessId Business ID
     * @param userId Employee user ID
     * @param request Update employee request
     * @return Updated business
     */
    @PutMapping("/{userId}")
    @Operation(
        summary = "Update a business employee",
        description = "Updates the role, status or description of an employee in the specified business"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Employee updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business or employee not found"
        )
    })
    public ResponseEntity<Business> updateEmployee(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId,
        @Parameter(description = "Employee user ID", required = true, example = "6507c123456789abcdef0001")
        @PathVariable String userId,
        @Parameter(description = "Update employee request", required = true)
        @Valid @RequestBody UpdateBusinessEmployeeRequest request
    ) {
        log.info("Update employee request received for business: {}, user: {}", businessId, userId);
        Business business = businessEmployeeService.updateEmployee(businessId, userId, request);
        return ResponseEntity.ok(business);
    }
    
    /**
     * Remove an employee from a business
     * @param businessId Business ID
     * @param userId Employee user ID
     * @return Updated business
     */
    @DeleteMapping("/{userId}")
    @Operation(
        summary = "Remove an employee from a business",
        description = "Removes an employee or partner from the specified business"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Employee removed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Business.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business or employee not found"
        )
    })
    public ResponseEntity<Business> removeEmployee(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId,
        @Parameter(description = "Employee user ID", required = true, example = "6507c123456789abcdef0001")
        @PathVariable String userId
    ) {
        log.info("Remove employee request received for business: {}, user: {}", businessId, userId);
        Business business = businessEmployeeService.removeEmployee(businessId, userId);
        return ResponseEntity.ok(business);
    }
    
    /**
     * Get all employees of a business
     * @param businessId Business ID
     * @return List of business employees
     */
    @GetMapping
    @Operation(
        summary = "Get all employees of a business",
        description = "Retrieves a list of all employees and partners working for the specified business"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Employees retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusinessEmployeeResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business not found"
        )
    })
    public ResponseEntity<List<BusinessEmployeeResponse>> getBusinessEmployees(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId
    ) {
        log.info("Get business employees request received for business: {}", businessId);
        List<BusinessEmployeeResponse> employees = businessEmployeeService.getBusinessEmployees(businessId);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * Get a specific employee of a business
     * @param businessId Business ID
     * @param userId Employee user ID
     * @return Business employee
     */
    @GetMapping("/{userId}")
    @Operation(
        summary = "Get a specific employee of a business",
        description = "Retrieves details of a specific employee or partner working for the specified business"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Employee found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BusinessEmployeeResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Business or employee not found"
        )
    })
    public ResponseEntity<BusinessEmployeeResponse> getBusinessEmployee(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId,
        @Parameter(description = "Employee user ID", required = true, example = "6507c123456789abcdef0001")
        @PathVariable String userId
    ) {
        log.info("Get business employee request received for business: {}, user: {}", businessId, userId);
        BusinessEmployeeResponse employee = businessEmployeeService.getBusinessEmployee(businessId, userId);
        return ResponseEntity.ok(employee);
    }
    
    /**
     * Check if a user is an employee of a business
     * @param businessId Business ID
     * @param userId User ID
     * @return true if user is an employee
     */
    @GetMapping("/{userId}/check")
    @Operation(
        summary = "Check if a user is an employee of a business",
        description = "Checks whether a specific user is an active employee or partner of the specified business"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Check completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "boolean")
            )
        )
    })
    public ResponseEntity<Boolean> isEmployeeOfBusiness(
        @Parameter(description = "Business ID", required = true, example = "6507c123456789abcdef0003")
        @PathVariable String businessId,
        @Parameter(description = "User ID", required = true, example = "6507c123456789abcdef0001")
        @PathVariable String userId
    ) {
        log.info("Check employee request received for business: {}, user: {}", businessId, userId);
        boolean isEmployee = businessEmployeeService.isEmployeeOfBusiness(businessId, userId);
        return ResponseEntity.ok(isEmployee);
    }
} 
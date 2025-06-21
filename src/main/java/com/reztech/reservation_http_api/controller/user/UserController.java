package com.reztech.reservation_http_api.controller.user;

import com.reztech.reservation_http_api.model.api.request.CreateUserRequest;
import com.reztech.reservation_http_api.model.entity.main.user.User;
import com.reztech.reservation_http_api.model.enums.UserType;
import com.reztech.reservation_http_api.service.user.UserService;
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
 * REST Controller for User operations
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management operations including create, read, update, and delete operations")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Create a new user
     * @param request Create user request
     * @return Created user
     */
    @PostMapping
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided information. The user will be used to make reservations."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid user data provided"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "User with this email already exists"
        )
    })
    public ResponseEntity<User> createUser(
        @Parameter(description = "User creation request", required = true)
        @Valid @RequestBody CreateUserRequest request
    ) {
        log.info("Creating user request received");
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    /**
     * Update an existing user
     * @param id User ID
     * @param request Update user request
     * @return Updated user
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing user",
        description = "Updates the user information with the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid user data provided"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<User> updateUser(
        @Parameter(description = "User ID", required = true, example = "6507c123456789abcdef0002")
        @PathVariable String id,
        @Parameter(description = "User update request", required = true)
        @Valid @RequestBody CreateUserRequest request
    ) {
        log.info("Update user request received for id: {}", id);
        User user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Get all users
     * @return List of all users
     */
    @GetMapping
    @Operation(
        summary = "Get all users",
        description = "Retrieves a list of all registered users in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        )
    })
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Get all users request received");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Find user by ID
     * @param id User ID
     * @return User
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Find user by ID",
        description = "Retrieves a specific user by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<User> findById(
        @Parameter(description = "User ID", required = true, example = "6507c123456789abcdef0002")
        @PathVariable String id
    ) {
        log.info("Find user by id request received for id: {}", id);
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Find user by email
     * @param email User email
     * @return User
     */
    @GetMapping("/email/{email}")
    @Operation(
        summary = "Find user by email",
        description = "Retrieves a specific user by their email address"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<User> findByEmail(
        @Parameter(description = "User email address", required = true, example = "ahmet.yilmaz@example.com")
        @PathVariable String email
    ) {
        log.info("Find user by email request received for email: {}", email);
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Find users by type
     * @param userType User type
     * @return List of users
     */
    @GetMapping("/type/{userType}")
    @Operation(
        summary = "Find users by type",
        description = "Retrieves all users of a specific type (CUSTOMER, EMPLOYEE, BUSINESS_OWNER, ADMIN)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Users found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        )
    })
    public ResponseEntity<List<User>> findByUserType(
        @Parameter(description = "User type", required = true, example = "BUSINESS_OWNER")
        @PathVariable UserType userType
    ) {
        log.info("Find users by type request received for type: {}", userType);
        List<User> users = userService.findByUserType(userType);
        return ResponseEntity.ok(users);
    }

    /**
     * Find user by email and type
     * @param email User email
     * @param userType User type
     * @return User
     */
    @GetMapping("/email/{email}/type/{userType}")
    @Operation(
        summary = "Find user by email and type",
        description = "Retrieves a specific user by their email address and user type"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<User> findByEmailAndUserType(
        @Parameter(description = "User email address", required = true, example = "owner@business.com")
        @PathVariable String email,
        @Parameter(description = "User type", required = true, example = "BUSINESS_OWNER")
        @PathVariable UserType userType
    ) {
        log.info("Find user by email: {} and type: {} request received", email, userType);
        User user = userService.findByEmailAndUserType(email, userType);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Delete user by ID
     * @param id User ID
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete user by ID",
        description = "Deletes a specific user by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "User deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "User ID", required = true, example = "6507c123456789abcdef0002")
        @PathVariable String id
    ) {
        log.info("Delete user request received for id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
} 
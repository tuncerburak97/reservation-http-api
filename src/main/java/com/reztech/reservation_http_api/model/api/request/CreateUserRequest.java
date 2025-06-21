package com.reztech.reservation_http_api.model.api.request;

import com.reztech.reservation_http_api.annotation.validator.ValidEmail;
import com.reztech.reservation_http_api.annotation.validator.ValidGsm;
import com.reztech.reservation_http_api.model.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Request DTO for creating users
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request model for creating a new user")
public class CreateUserRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "User's first name", example = "Ahmet", required = true, minLength = 2, maxLength = 50)
    private String name;
    
    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Schema(description = "User's last name", example = "Yılmaz", required = true, minLength = 2, maxLength = 50)
    private String surname;
    
    @NotBlank(message = "GSM number is required")
    @ValidGsm
    @Schema(description = "User's GSM number in Turkish format", example = "05551234567", required = true, pattern = "^05\\d{9}$")
    private String gsm;
    
    @NotBlank(message = "Email is required")
    @ValidEmail
    @Schema(description = "User's email address", example = "ahmet.yilmaz@example.com", required = true, format = "email")
    private String email;

    @NotNull(message = "User type is required")
    @Schema(description = "Type of user indicating their role in the system", example = "CUSTOMER", required = true, allowableValues = {"CUSTOMER", "EMPLOYEE", "BUSINESS_OWNER", "ADMIN"})
    private UserType userType;
} 
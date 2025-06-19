package com.reztech.reservation_http_api.model.api.request;

import com.reztech.reservation_http_api.annotation.validator.ValidEmail;
import com.reztech.reservation_http_api.annotation.validator.ValidGsm;
import com.reztech.reservation_http_api.model.enums.OwnerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Request DTO for creating owners
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request model for creating a new business owner")
public class CreateOwnerRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "Owner's first name", example = "Mehmet", required = true, minLength = 2, maxLength = 50)
    private String name;
    
    @NotBlank(message = "Lastname is required")
    @Size(min = 2, max = 50, message = "Lastname must be between 2 and 50 characters")
    @Schema(description = "Owner's last name", example = "Özkan", required = true, minLength = 2, maxLength = 50)
    private String lastname;
    
    @NotBlank(message = "GSM number is required")
    @ValidGsm
    @Schema(description = "Owner's GSM number in Turkish format", example = "05559876543", required = true, pattern = "^05\\d{9}$")
    private String gsm;
    
    @NotBlank(message = "Email is required")
    @ValidEmail
    @Schema(description = "Owner's email address", example = "mehmet.ozkan@business.com", required = true, format = "email")
    private String email;
    
    @NotNull(message = "Owner type is required")
    @Schema(description = "Type of owner indicating their role in the system", example = "ADMIN", required = true, allowableValues = {"ADMIN", "NORMAL_USER"})
    private OwnerType ownerType;
} 
package com.reztech.reservation_http_api.model.api.request;

import com.reztech.reservation_http_api.model.entity.ContactInfo;
import com.reztech.reservation_http_api.model.entity.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Request DTO for creating businesses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request model for creating a new business")
public class CreateBusinessRequest {
    
    @NotBlank(message = "Business name is required")
    @Size(min = 2, max = 100, message = "Business name must be between 2 and 100 characters")
    @Schema(description = "Business name", example = "Güzellik Salonu", required = true, minLength = 2, maxLength = 100)
    private String name;
    
    @NotNull(message = "Location is required")
    @Valid
    @Schema(description = "Business location information", required = true)
    private Location location;
    
    @NotBlank(message = "Owner ID is required")
    @Schema(description = "ID of the business owner", example = "6507c123456789abcdef0001", required = true)
    private String ownerId;
    
    @Valid
    @Schema(description = "Business contact information")
    private ContactInfo contactInfo;
} 
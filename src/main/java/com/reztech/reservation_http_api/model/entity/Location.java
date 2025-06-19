package com.reztech.reservation_http_api.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Location entity for Google Places integration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Location model for business address and Google Places integration")
public class Location {
    
    @NotBlank(message = "Google ID is required")
    @Schema(description = "Google Places ID", example = "ChIJd8ZtaV9_xkcR8MqGJ7tD5Cs", required = true)
    private String googleId;
    
    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 200, message = "Address must be between 10 and 200 characters")
    @Schema(description = "Full address of the location", example = "Atatürk Cad. No:123 Kadıköy/İstanbul", required = true, minLength = 10, maxLength = 200)
    private String address;
    
    @Schema(description = "Latitude coordinate", example = "41.0082")
    private Double latitude;
    
    @Schema(description = "Longitude coordinate", example = "28.9784")
    private Double longitude;
} 
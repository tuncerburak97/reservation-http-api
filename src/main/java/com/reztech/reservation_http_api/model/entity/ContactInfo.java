package com.reztech.reservation_http_api.model.entity;

import com.reztech.reservation_http_api.annotation.validator.ValidEmail;
import com.reztech.reservation_http_api.annotation.validator.ValidGsm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Contact information entity for businesses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Contact information model for businesses")
public class ContactInfo {
    
    @ValidGsm
    @Schema(description = "Business phone number in Turkish format", example = "05551234567", pattern = "^05\\d{9}$")
    private String phone;
    
    @ValidEmail
    @Schema(description = "Business email address", example = "info@business.com", format = "email")
    private String email;
    
    @Size(max = 100, message = "Website URL must be less than 100 characters")
    @Schema(description = "Business website URL", example = "https://www.business.com", maxLength = 100)
    private String website;
    
    @Size(max = 200, message = "Address must be less than 200 characters")
    @Schema(description = "Business address", example = "Atatürk Cad. No:123 Kadıköy/İstanbul", maxLength = 200)
    private String address;
} 
package com.reztech.reservation_http_api.model.api.request;

import com.reztech.reservation_http_api.model.enums.BusinessRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Request model for adding an employee to a business
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBusinessEmployeeRequest {
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Role is required")
    private BusinessRole role;
    
    private String description;
} 
package com.reztech.reservation_http_api.model.api.request;

import com.reztech.reservation_http_api.model.enums.BusinessRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Request model for updating a business employee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBusinessEmployeeRequest {
    
    private BusinessRole role;
    
    private Boolean active;
    
    private String description;
} 
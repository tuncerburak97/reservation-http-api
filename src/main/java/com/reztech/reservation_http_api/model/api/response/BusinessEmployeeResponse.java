package com.reztech.reservation_http_api.model.api.response;

import com.reztech.reservation_http_api.model.enums.BusinessRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Response model for business employee information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessEmployeeResponse {
    
    private String userId;
    private String name;
    private String surname;
    private String email;
    private String gsm;
    private BusinessRole role;
    private LocalDateTime joinedAt;
    private boolean active;
    private String description;
} 
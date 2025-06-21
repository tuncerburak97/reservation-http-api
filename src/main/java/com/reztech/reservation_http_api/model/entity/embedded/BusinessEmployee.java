package com.reztech.reservation_http_api.model.entity.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Business employee embedded entity
 * Represents an employee/partner associated with a business
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessEmployee {
    // Employee user ID
    private String userId;
    
    // When employee was added to business
    private LocalDateTime joinedAt;
    
    // Is employee active
    private boolean active;
}
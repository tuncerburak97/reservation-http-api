package com.reztech.reservation_http_api.core.validation;

import com.reztech.reservation_http_api.annotation.validator.FutureDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

/**
 * Future date validator implementation
 */
public class FutureDateValidator implements ConstraintValidator<FutureDate, Instant> {
    
    @Override
    public void initialize(FutureDate constraintAnnotation) {
        // No initialization needed
    }
    
    @Override
    public boolean isValid(Instant instant, ConstraintValidatorContext context) {
        if (instant == null) {
            return true; // Let @NotNull handle null validation
        }
        
        return instant.isAfter(Instant.now());
    }
} 
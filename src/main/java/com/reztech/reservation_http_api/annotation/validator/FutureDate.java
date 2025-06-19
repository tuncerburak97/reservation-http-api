package com.reztech.reservation_http_api.annotation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom future date validation annotation
 */
@Documented
@Constraint(validatedBy = com.reztech.reservation_http_api.core.validation.FutureDateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureDate {
    
    String message() default "Date must be in the future";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
} 
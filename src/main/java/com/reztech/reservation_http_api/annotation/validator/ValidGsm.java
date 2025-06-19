package com.reztech.reservation_http_api.annotation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom GSM number validation annotation
 */
@Documented
@Constraint(validatedBy = com.reztech.reservation_http_api.core.validation.GsmValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGsm {
    
    String message() default "Invalid GSM number format";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
} 
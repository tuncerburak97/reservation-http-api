package com.reztech.reservation_http_api.core.validation;

import com.reztech.reservation_http_api.annotation.validator.ValidGsm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * GSM number validator implementation for Turkish phone numbers
 */
public class GsmValidator implements ConstraintValidator<ValidGsm, String> {
    
    // Turkish mobile phone pattern: starts with +90 or 0, followed by 5XX XXXXXXX
    private static final String GSM_PATTERN = 
        "^(\\+90|0)?(5\\d{2})(\\d{3})(\\d{2})(\\d{2})$";
    
    private static final Pattern pattern = Pattern.compile(GSM_PATTERN);
    
    @Override
    public void initialize(ValidGsm constraintAnnotation) {
        // No initialization needed
    }
    
    @Override
    public boolean isValid(String gsm, ConstraintValidatorContext context) {
        if (gsm == null || gsm.trim().isEmpty()) {
            return false;
        }
        
        // Remove spaces and dashes for validation
        String cleanGsm = gsm.replaceAll("[\\s-]", "");
        
        return pattern.matcher(cleanGsm).matches();
    }
} 
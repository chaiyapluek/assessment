package com.kbtg.bootcamp.posttest.annotation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumberStringValidator implements ConstraintValidator<NumberString, String>{

    private String message;
    private int min;
    private int max;

    @Override
    public void initialize(NumberString constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("cannot be null or empty.")
                    .addConstraintViolation();
            return false;
        }

        if (value.length() < min || value.length() > max) {
            String msg;
            if (min == max) {
                msg = "must be " + min + " characters long.";
            } else {
                msg = "must be between " + min + " and " + max + " characters long.";
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(msg)
                    .addConstraintViolation();
            return false;
        }

        if (!Pattern.matches("^[0-9]*$", value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must be a number.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
    
}

package com.kbtg.bootcamp.posttest.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NumberStringValidator.class)
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface NumberString {
    String message() default "{javax.validation.constraints.NotBlank.message}";

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}

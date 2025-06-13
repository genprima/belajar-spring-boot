package com.nostratech.belajarspringboot.spring.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nostratech.belajarspringboot.spring.validator.NameValidator;

import jakarta.validation.Constraint;

@Constraint(validatedBy = NameValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {

    String message() default "Name cannot contains number";

    // Class<?>[] groups() default {};

    // Class<? extends Payload>[] payload() default {};
}

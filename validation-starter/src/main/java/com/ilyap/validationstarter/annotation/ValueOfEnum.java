package com.ilyap.validationstarter.annotation;

import com.ilyap.validationstarter.validator.ValueOfEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValueOfEnumValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueOfEnum {

    Class<? extends Enum<?>> value();

    String message() default "string must be any value of enum {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


package com.ilyap.taskmanager.validation.impl;

import com.ilyap.taskmanager.validation.ValueOfEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    private List<String> validValues;

    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        validValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return validValues.contains(value);
    }
}

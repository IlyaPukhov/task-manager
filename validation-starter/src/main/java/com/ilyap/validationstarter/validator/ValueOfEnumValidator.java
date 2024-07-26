package com.ilyap.validationstarter.validator;

import com.ilyap.validationstarter.annotation.ValueOfEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    private List<String> validValues;

    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        validValues = Arrays.stream(constraintAnnotation.value().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return validValues.contains(value.toUpperCase());
    }
}

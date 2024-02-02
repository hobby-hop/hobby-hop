package com.hobbyhop.domain.user.dto.validCustom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullablePatternValidator implements ConstraintValidator<NullablePattern, String> {

    private int min, max;
    private String regexp, msg1, msg2, massage;

    @Override
    public void initialize(NullablePattern constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.regexp = constraintAnnotation.regexp();
        this.msg1 = constraintAnnotation.msg1();
        this.msg2 = constraintAnnotation.msg2();
        this.massage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.isBlank()) {
            return true;
        }
        boolean isValid = true;
        if (!value.matches(regexp)) {
            massage += msg1;
            isValid = false;
        }
        if (value.length() > max || value.length() < min) {
            if (!massage.isBlank()) {
                massage += "\n";
            }
            massage += msg2;
            isValid = false;
        }
        return isValid;
    }
}
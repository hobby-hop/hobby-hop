package com.hobbyhop.domain.user.dto.validCustom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NullablePatternValidator.class})
public @interface NullablePattern {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String msg1() default "";
    String msg2() default "";
    String regexp() default ".*";
    int min() default 0;
    int max() default 64;
}
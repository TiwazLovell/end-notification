package com.endava.notificationsservice.validations.annotations;

import com.endava.notificationsservice.enums.ValidationType;
import com.endava.notificationsservice.validations.validators.ArgumentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ArgumentValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidParameter {
    /**
     * This is the type of parameter which will be validated
     */
    ValidationType type();

    /**
     * This will be the error message
     */
    public String message() default "There is no entity with id'${validatedValue}'";

    /**
     * This represents the group of constraints
     */
    public Class<?>[] groups() default {};

    /**
     * This is the additional information about annotation
     */
    public Class<? extends Payload>[] payload() default {};
}

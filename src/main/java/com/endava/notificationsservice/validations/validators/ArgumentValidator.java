package com.endava.notificationsservice.validations.validators;

import com.endava.notificationsservice.enums.ValidationType;
import com.endava.notificationsservice.repository.NotificationRepository;
import com.endava.notificationsservice.validations.annotations.ValidParameter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class ArgumentValidator implements ConstraintValidator<ValidParameter, UUID> {

    private final NotificationRepository notificationRepository;

    private ValidationType validationType;

    @Override
    public void initialize(ValidParameter constraintAnnotation) {
        validationType = constraintAnnotation.type();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UUID entityId, ConstraintValidatorContext constraintValidatorContext) {
        if (validationType.equals(ValidationType.EMPLOYEE)) {
            return notificationRepository.existsNotificationEntitiesByEmployeeId(entityId);
        }
        if(validationType.equals(ValidationType.NOTIFICATION)) {
            return notificationRepository.existsNotificationEntityByNotificationId(entityId);
        }
        return false;
    }
}

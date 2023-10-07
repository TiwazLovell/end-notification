package com.endava.notificationsservice.dto;

import com.endava.notificationsservice.enums.NotificationType;
import com.endava.notificationsservice.enums.Status;
import com.endava.notificationsservice.enums.UserType;
import com.endava.notificationsservice.enums.ValidationType;
import com.endava.notificationsservice.validations.annotations.ValidParameter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class NotificationRequestDTO {

    @NotNull
    UUID notificationId;
    @NotNull
    @NotEmpty
    @ValidParameter(type = ValidationType.EMPLOYEE)
    UUID userID;
    @NotNull
    @NotEmpty
    UserType userType;
    @NotNull
    @NotEmpty
    String message;
    @NotNull
    @NotEmpty
    Status status;
    @NotNull
    NotificationType notificationType;
    Map<String, Object> additionalData;
}

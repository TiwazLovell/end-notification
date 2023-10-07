package com.endava.notificationsservice.dto;

import com.endava.notificationsservice.enums.NotificationType;
import com.endava.notificationsservice.enums.Status;
import com.endava.notificationsservice.enums.UserType;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Value
public class NotificationDTO implements Serializable {

    UUID notificationId;
    String message;
    UUID employeeId;
    Status status;
    UserType userType;
    NotificationType notificationType;
    LocalDateTime notificationDate;

}


package com.endava.notificationsservice.dto;

import com.endava.notificationsservice.enums.Status;
import com.endava.notificationsservice.enums.UserType;
import com.endava.notificationsservice.utils.jackson.NotificationStatusSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class NotificationResponseDTO {

    UUID notificationId;
    UUID userID;
    UserType userType;
    String message;
    @JsonSerialize(using = NotificationStatusSerializer.class)
    Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd hh:mm:ss")
    LocalDateTime notificationDate;
    Map<String, Object> additionalData;

}

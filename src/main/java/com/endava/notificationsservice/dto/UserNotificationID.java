package com.endava.notificationsservice.dto;

import com.endava.notificationsservice.enums.UserType;
import lombok.Value;

import java.util.UUID;

@Value
public class UserNotificationID {

    UUID userID;

    UserType userType;

}

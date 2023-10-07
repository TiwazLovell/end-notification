package com.endava.notificationsservice.exceptions;

import java.util.UUID;

public class NotificationNotFoundException extends RuntimeException{

    public NotificationNotFoundException(UUID id) {
        super("Unable to find notification with id: " + id);
    }
}

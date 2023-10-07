package com.endava.notificationsservice.exceptions;

public class ServerSentEventException extends RuntimeException {

    public ServerSentEventException(String message){
        super(message);
    }
}

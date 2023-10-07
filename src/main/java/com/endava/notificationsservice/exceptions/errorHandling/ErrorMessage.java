package com.endava.notificationsservice.exceptions.errorHandling;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ErrorMessage {

    private final Integer statusCode;
    private final List<String> message;
}

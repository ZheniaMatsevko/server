package com.example.eventsmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationException extends RuntimeException {
    public ValidationException() {
        super();
    }
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(Throwable cause) {
        super(cause);
    }

}
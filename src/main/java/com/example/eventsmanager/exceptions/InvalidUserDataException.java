package com.example.eventsmanager.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidUserDataException extends RuntimeException {

    public InvalidUserDataException() {
        super();
    }

    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserDataException(String message) {
        super(message);
    }

    public InvalidUserDataException(Throwable cause) {
        super(cause);
    }
}


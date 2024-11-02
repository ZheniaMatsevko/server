package com.example.eventsmanager.exceptions;

public class WrongPasswordFormat extends RuntimeException {
    private static final String ERROR_CODE = "ERR002";
    public WrongPasswordFormat(String message) {
        super(message);
    }

    public String getErrorCode() {
        return ERROR_CODE;
    }
}

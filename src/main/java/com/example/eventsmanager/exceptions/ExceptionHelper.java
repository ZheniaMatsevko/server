package com.example.eventsmanager.exceptions;

import jakarta.validation.ConstraintViolation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Set;

public class ExceptionHelper {
    public static String formErrorMessage(BindingResult bindingResult) {
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        String message = "Validation failed: ";
        for (ObjectError o : allErrors){
            message+=o.getDefaultMessage()+"\n";
        }
        return message;
    }
    public static <T> String formErrorMessage(Set<ConstraintViolation<T>> violations) {
        String message = "Validation failed: ";
        for (ConstraintViolation<T> violation : violations) {
            message += violation.getPropertyPath() + " " + violation.getMessage() + "\n";
        }
        return message;
    }
}

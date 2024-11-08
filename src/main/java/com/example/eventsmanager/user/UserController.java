package com.example.eventsmanager.user;

import com.example.eventsmanager.exceptions.ExceptionHelper;
import com.example.eventsmanager.exceptions.InvalidOldPasswordException;
import com.example.eventsmanager.user.changePassword.ChangePasswordDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new")
    public UserDto createUser(@RequestPart(value = "file", required = false) MultipartFile file,
                                             @RequestPart("user") @Valid String userJson, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }

        try {
            UserDto userDto = new ObjectMapper().readValue(userJson, UserDto.class);
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

            if (!violations.isEmpty()) {
                String message = ExceptionHelper.formErrorMessage(violations);
                throw new javax.validation.ValidationException(message);
            }else{
                return userService.createUser(userDto,file);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id,@RequestPart(value = "file", required = false) MultipartFile file,
                                     @RequestPart("user") @Valid String userJson, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        try {
            UserUpdateDto userUpdateDto = new ObjectMapper().readValue(userJson, UserUpdateDto.class);
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<UserUpdateDto>> violations = validator.validate(userUpdateDto);

            if (!violations.isEmpty()) {
                String message = ExceptionHelper.formErrorMessage(violations);
                throw new javax.validation.ValidationException(message);
            }else{
                userUpdateDto.setId(id);
                return userService.updateUser(userUpdateDto,file);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User deleted with ID: {}", id);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Retrieving user with ID: {}", userId);
        return userService.getUserById(userId);
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordDto request) {
        try {
            userService.changePassword(request);
            return ResponseEntity.ok("The password was changed successfully");
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (InvalidOldPasswordException e) {
            e.printStackTrace();
            String errorCode = e.getErrorCode();
            String errorMessage = e.getMessage();
            return ResponseEntity.badRequest().body(errorCode + ": " + errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error to change password.");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

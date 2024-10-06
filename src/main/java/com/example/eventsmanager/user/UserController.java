package com.example.eventsmanager.user;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public UserRequestDto createUser(@RequestBody @Valid UserRequestDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        UserDto createdUser = userService.createUser(IUserMapper.INSTANCE.requestDtoToDto(user));
        log.info("User created with ID: {}", createdUser.getId());
        return IUserMapper.INSTANCE.dtoToRequestDto(createdUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.info("UserEntity deleted with ID: {}", id);
    }

    @GetMapping("/{userId}")
    public UserRequestDto getUserById(@PathVariable Long userId) {
        log.info("Retrieving user with ID: {}", userId);
        Optional<UserDto> user = userService.getUserById(userId);
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId);
        return IUserMapper.INSTANCE.dtoToRequestDto(user.get());
    }

    @GetMapping("/username/{username}")
    public UserRequestDto getUserByUsername(@PathVariable String username) {
        log.info("Retrieving user with username: {}", username);
        return IUserMapper.INSTANCE.dtoToRequestDto(userService.getUserByUsername(username));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        if (e instanceof ConstraintViolationException)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        else if (e instanceof ResponseStatusException)
            return new ResponseEntity<>(e.getMessage(), ((ResponseStatusException) e).getStatusCode());

        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

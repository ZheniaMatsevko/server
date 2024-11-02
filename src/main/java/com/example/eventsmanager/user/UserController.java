package com.example.eventsmanager.user;

import com.example.eventsmanager.exceptions.ExceptionHelper;
import com.example.eventsmanager.exceptions.InvalidOldPasswordException;
import com.example.eventsmanager.utils.ChangePasswordRequestDto;
import com.example.eventsmanager.utils.IChangePasswordMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public UserRequestDto createUser(@RequestBody @Valid UserRequestDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        UserDto createdUser = userService.createUser(IUserMapper.INSTANCE.requestDtoToDto(user));
        log.info("User created with ID: {}", createdUser.getId());
        return IUserMapper.INSTANCE.dtoToRequestDto(createdUser);
    }

    @PutMapping("/{id}")
    public UserRequestDto updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        user.setId(id);
        UserDto updatedUser = userService.updateUser(IUserMapper.INSTANCE.requestDtoToDto(user));
        log.info("User updated with ID: {}", updatedUser.getId());
        return IUserMapper.INSTANCE.dtoToRequestDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User deleted with ID: {}", id);
    }

    @GetMapping("/{userId}")
    public UserRequestDto getUserById(@PathVariable Long userId) {
        log.info("Retrieving user with ID: {}", userId);
        return IUserMapper.INSTANCE.dtoToRequestDto(userService.getUserById(userId));
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDto request) {
        try {
            userService.changePassword(IChangePasswordMapper.INSTANCE.requestDtoToDto(request));
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

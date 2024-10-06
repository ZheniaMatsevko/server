package com.example.eventsmanager.user;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserDto createUser(UserDto user);

    void deleteUser(Long userId);

    Optional<UserDto> getUserById(Long userId);

    UserDto getUserByEmail(String email);

    //List<UserDto> getAllUsers();
}

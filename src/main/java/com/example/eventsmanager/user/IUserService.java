package com.example.eventsmanager.user;

import com.example.eventsmanager.utils.ChangePasswordDto;

public interface IUserService {
    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);

    UserDto getUserByEmail(String email);

    void changePassword(ChangePasswordDto changePassword);
}

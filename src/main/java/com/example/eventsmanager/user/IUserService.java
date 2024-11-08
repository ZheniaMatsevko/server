package com.example.eventsmanager.user;

import com.example.eventsmanager.user.changePassword.ChangePasswordDto;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    UserDto createUser(UserDto user, MultipartFile file);

    UserDto updateUser(UserUpdateDto user, MultipartFile file);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);

    UserDto getUserByEmail(String email);

    void changePassword(ChangePasswordDto changePassword);
}

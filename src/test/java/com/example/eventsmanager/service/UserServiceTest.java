package com.example.eventsmanager.service;

import com.example.eventsmanager.exceptions.InvalidOldPasswordException;
import com.example.eventsmanager.user.*;
import com.example.eventsmanager.utils.ChangePasswordDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserDto userDto = new UserDto();
        userDto.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto createdUser = userService.createUser(userDto, null);

        assertNotNull(createdUser);
        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).encode("password");
    }

    @Test
    void testUpdateUser_Success() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(1L);
        userUpdateDto.setUsername("username2");
        userUpdateDto.setEmail("newEmail@example.com");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setUsername("username2");
        existingUser.setEmail("oldEmail@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingUser);

        UserDto updatedUser = userService.updateUser(userUpdateDto, null);

        assertNotNull(updatedUser);
        assertEquals("newEmail@example.com", existingUser.getEmail());
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userUpdateDto, null));
    }

    @Test
    void testDeleteUser_UserExists() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
        verify(userRepository).existsById(1L);
    }

    @Test
    void testGetUserById_UserExists() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        UserDto foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetUserByUsername_UserExists() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

        UserDto foundUser = userService.getUserByUsername("username");

        assertNotNull(foundUser);
        assertEquals("username", foundUser.getUsername());
    }

    @Test
    void testChangePassword_Success() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setId(1L);
        changePasswordDto.setOldPassword("oldPassword");
        changePasswordDto.setNewPassword("newPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPassword("encodedOldPassword");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        userService.changePassword(changePasswordDto);

        verify(userRepository).save(userEntity);
        assertEquals("encodedNewPassword", userEntity.getPassword());
    }

    @Test
    void testChangePassword_InvalidOldPassword() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setId(1L);
        changePasswordDto.setOldPassword("wrongPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setPassword("encodedOldPassword");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidOldPasswordException.class, () -> userService.changePassword(changePasswordDto));
    }

    @Test
    void testChangePassword_UserNotFound() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.changePassword(changePasswordDto));
    }
}

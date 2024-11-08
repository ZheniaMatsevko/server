package com.example.eventsmanager.repository;

import com.example.eventsmanager.user.IUserRepository;
import com.example.eventsmanager.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUpData() {
        // Create and save users
        user1 = new UserEntity();
        user1.setUsername("john_doe");
        user1.setEmail("john@example.com");
        user1.setPassword("password123");
        user1.setFirstname("John");
        user1.setLastname("Doe");
        user1 = userRepository.saveAndFlush(user1);

        user2 = new UserEntity();
        user2.setUsername("jane_doe");
        user2.setEmail("jane@example.com");
        user2.setPassword("password456");
        user2.setFirstname("Jane");
        user2.setLastname("Doe");
        user2.setBookedEvents(new ArrayList<>());
        user2 = userRepository.saveAndFlush(user2);
    }

    @Test
    void testSaveUser_Success() {
        UserEntity newUser = new UserEntity();
        newUser.setUsername("alice_smith");
        newUser.setEmail("alice@example.com");
        newUser.setPassword("password789");
        newUser.setFirstname("Alice");
        newUser.setLastname("Smith");

        UserEntity savedUser = userRepository.save(newUser);
        assertNotNull(savedUser.getId(), "Expected saved user to have an ID");
        assertEquals("alice_smith", savedUser.getUsername());
    }

    @Test
    void testFindById_Found() {
        Optional<UserEntity> foundUser = userRepository.findById(user1.getId());
        assertTrue(foundUser.isPresent(), "Expected to find user by ID");
        assertEquals(user1.getId(), foundUser.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        Optional<UserEntity> foundUser = userRepository.findById(999L);
        assertFalse(foundUser.isPresent(), "Expected no user to be found for a non-existing ID");
    }

    @Test
    void testFindByUsername_Found() {
        Optional<UserEntity> foundUser = userRepository.findByUsername("john_doe");
        assertTrue(foundUser.isPresent(), "Expected to find user by username");
        assertEquals("john_doe", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<UserEntity> foundUser = userRepository.findByUsername("non_existing_user");
        assertFalse(foundUser.isPresent(), "Expected no user to be found for a non-existing username");
    }

    @Test
    void testFindByEmail_Found() {
        Optional<UserEntity> foundUser = userRepository.findByEmail("john@example.com");
        assertTrue(foundUser.isPresent(), "Expected to find user by email");
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<UserEntity> foundUser = userRepository.findByEmail("non_existing@example.com");
        assertFalse(foundUser.isPresent(), "Expected no user to be found for a non-existing email");
    }

    @Test
    void testDeleteById_Success() {
        Long userId = user2.getId();
        userRepository.deleteById(userId);

        Optional<UserEntity> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent(), "Expected user to be deleted");
    }

    @Test
    void testFindAll_Success() {
        List<UserEntity> users = userRepository.findAll();
        assertEquals(2, users.size(), "Expected to retrieve all users");
    }
}

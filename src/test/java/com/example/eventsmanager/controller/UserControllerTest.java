package com.example.eventsmanager.controller;

import com.example.eventsmanager.security.auth.jwt.JwtAuthenticationFilter;
import com.example.eventsmanager.security.auth.jwt.JwtService;
import com.example.eventsmanager.user.*;
import com.example.eventsmanager.utils.ChangePasswordRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private IUserMapper userMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private final String USER_USERNAME = "test_user";

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testCreateUser_ShouldReturn200() throws Exception {
        UserRequestDto userDto = createUserRequestDto();
        String userJson = objectMapper.writeValueAsString(userDto);

        MockMultipartFile mockFile = new MockMultipartFile("file", "profile.jpg", "image/jpeg", new byte[0]);
        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json", userJson.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/new")
                        .file(mockFile)
                        .file(userPart)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testCreateUser_ShouldReturn500_WhenValidationFails() throws Exception {
        String invalidUserJson = "{}"; // Invalid JSON that should fail validation

        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json", invalidUserJson.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/new")
                        .file(userPart)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testUpdateUser_ShouldReturn200() throws Exception {
        UserUpdateDto userDto = createUserUpdateDto();
        String userJson = objectMapper.writeValueAsString(userDto);

        MockMultipartFile mockFile = new MockMultipartFile("file", "profile.png", "image/png", new byte[0]);
        MockMultipartFile userPart = new MockMultipartFile("user", "", "application/json", userJson.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/{id}", 1)
                        .file(mockFile)
                        .file(userPart)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testDeleteUser_ShouldReturn200() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(userService).deleteUser(1L);
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testGetUserById_ShouldReturn200() throws Exception {
        UserRequestDto userDto = createUserRequestDto();
        userDto.setId(1L);

        when(userService.getUserById(userDto.getId())).thenReturn(IUserMapper.INSTANCE.requestDtoToDto(userDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testChangePassword_ShouldReturn200() throws Exception {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(1L, "Password1", "Password2");
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/password")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("The password was changed successfully"));
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testChangePassword_InvalidPassword_ShouldReturn500() throws Exception {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(1L, "oldPassword", "newPassword");
        String jsonRequest = objectMapper.writeValueAsString(changePasswordRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/password")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isInternalServerError());
    }

    // Helper method to create a sample UserRequestDto
    private UserRequestDto createUserRequestDto() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUsername("test_user");
        userRequestDto.setEmail("user@example.com");
        userRequestDto.setPassword("password123");
        userRequestDto.setFirstname("firstname");
        userRequestDto.setLastname("lastname");

        return userRequestDto;
    }

    private UserUpdateDto createUserUpdateDto() {
        UserUpdateDto userRequestDto = new UserUpdateDto();
        userRequestDto.setId(1L);
        userRequestDto.setUsername("test_user");
        userRequestDto.setEmail("user@example.com");
        userRequestDto.setFirstname("firstname");
        userRequestDto.setLastname("lastname");

        return userRequestDto;
    }
}

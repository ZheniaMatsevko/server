package com.example.eventsmanager.controller;

import com.example.eventsmanager.event.EventController;
import com.example.eventsmanager.event.EventDto;
import com.example.eventsmanager.event.EventService;
import com.example.eventsmanager.security.auth.jwt.JwtAuthenticationFilter;
import com.example.eventsmanager.security.auth.jwt.JwtService;
import com.example.eventsmanager.user.UserDto;
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

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

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
    public void testMissingEndpoint_shouldReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/nonexistent/endpoint")
                        .with(SecurityMockMvcRequestPostProcessors.user(USER_USERNAME))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testGetAllEvents_ShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testDeleteEvent_ShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteEvent_Unauthorized_ShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testGetAllEventsByOrganiserId_ShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events/organiser/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testGetAllEventsByParticipantId_ShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events/participant/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testGetEventById_ShouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/events/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testCreateEvent_shouldReturn200() throws Exception {
        EventDto eventDto = createEventDto();
        String eventJson = objectMapper.writeValueAsString(eventDto);

        MockMultipartFile mockFile = new MockMultipartFile("file", "", "application/json", new byte[0]);

        MockMultipartFile eventPart = new MockMultipartFile("event", "", "application/json", eventJson.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/events/new")
                        .file(mockFile)
                        .file(eventPart)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testUpdateEvent_shouldReturn200() throws Exception {
        EventDto eventDto = createEventDto();
        String eventJson = objectMapper.writeValueAsString(eventDto);

        MockMultipartFile mockFile = new MockMultipartFile("file", "test.png", "image/png", new byte[0]);

        MockMultipartFile eventPart = new MockMultipartFile("event", "", "application/json", eventJson.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/events/{id}", 1)
                        .file(mockFile)
                        .file(eventPart)  // Add event as a MockMultipartFile
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
    public void testRegisterUserForEvent_ShouldReturn200() throws Exception {
        Long eventId = 1L;
        Long userId = 1L;

        // Simulate successful registration
        doNothing().when(eventService).registerUserForEvent(userId, eventId);

        mockMvc.perform(MockMvcRequestBuilders.put("/events/{eventId}/register/{userId}", eventId, userId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered for the event successfully."));

        verify(eventService).registerUserForEvent(userId, eventId);
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testRegisterUserForEvent_ShouldReturn500_WhenErrorOccurs() throws Exception {
        Long eventId = 1L;
        Long userId = 1L;

        // Simulate an error during registration
        doThrow(new RuntimeException("Database error")).when(eventService).registerUserForEvent(userId, eventId);

        mockMvc.perform(MockMvcRequestBuilders.put("/events/{eventId}/register/{userId}", eventId, userId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to register user for the event."));

        verify(eventService).registerUserForEvent(userId, eventId);
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testUnregisterUserFromEvent_ShouldReturn200() throws Exception {
        Long eventId = 1L;
        Long userId = 1L;

        // Simulate successful unregistration
        doNothing().when(eventService).unregisterUserFromEvent(userId, eventId);

        mockMvc.perform(MockMvcRequestBuilders.put("/events/{eventId}/unregister/{userId}", eventId, userId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User unregistered from the event successfully."));

        verify(eventService).unregisterUserFromEvent(userId, eventId);
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testUnregisterUserFromEvent_ShouldReturn500_WhenErrorOccurs() throws Exception {
        Long eventId = 1L;
        Long userId = 1L;

        // Simulate an error during unregistration
        doThrow(new RuntimeException("Database error")).when(eventService).unregisterUserFromEvent(userId, eventId);

        mockMvc.perform(MockMvcRequestBuilders.put("/events/{eventId}/unregister/{userId}", eventId, userId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to unregister user from the event."));

        verify(eventService).unregisterUserFromEvent(userId, eventId);
    }
    private EventDto createEventDto() {
        EventDto eventRequestDto = new EventDto();
        eventRequestDto.setCaption("Sample Event Caption");
        eventRequestDto.setDateTime(LocalDateTime.of(2024, 12, 12, 10, 0));
        eventRequestDto.setPrice(10.0f);
        eventRequestDto.setCapacity(100);
        eventRequestDto.setDescription("Sample description");
        eventRequestDto.setOrganiser(new UserDto(1L, "username", "password123", "user@example.com", "Firstname", "Lastname", "http://example.com/profile.jpg"));

        return eventRequestDto;
    }
}

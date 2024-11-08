package com.example.eventsmanager.controller;

import com.example.eventsmanager.event.EventDto;
import com.example.eventsmanager.review.ReviewController;
import com.example.eventsmanager.review.ReviewDto;
import com.example.eventsmanager.review.ReviewService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

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
    public void testAddReview_ShouldReturn200() throws Exception {
        ReviewDto reviewRequestDto = new ReviewDto(1L, new UserDto(), 2, "comment");
        String jsonRequest = objectMapper.writeValueAsString(reviewRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews/{eventId}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USER_USERNAME)
    public void testDeleteReview_ShouldReturn200() throws Exception {
        when(reviewService.removeReview(1L, 1L)).thenReturn(new EventDto());

        mockMvc.perform(MockMvcRequestBuilders.delete("/reviews/{reviewId}/{eventId}", 1,1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(reviewService).removeReview(1L,1L);
    }

}

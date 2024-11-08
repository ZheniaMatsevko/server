package com.example.eventsmanager.service;

import com.example.eventsmanager.event.EventDto;
import com.example.eventsmanager.event.EventEntity;
import com.example.eventsmanager.event.EventService;
import com.example.eventsmanager.event.IEventRepository;
import com.example.eventsmanager.user.IUserRepository;
import com.example.eventsmanager.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private IEventRepository eventRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private EventService eventService;

    private EventDto eventDto;
    private EventEntity eventEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock data for testing
        eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setCaption("Sample Event");

        eventEntity = new EventEntity();
        eventEntity.setId(1L);
        eventEntity.setCaption("Sample Event");

        userEntity = new UserEntity();
        userEntity.setId(1L);
    }

    @Test
    void testAddEvent_Success() {
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(file.isEmpty()).thenReturn(true); // Assume no file is uploaded

        EventDto result = eventService.addEvent(eventDto, file);

        assertNotNull(result);
        assertEquals("Sample Event", result.getCaption());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testUpdateEvent_Success() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));
        when(eventRepository.save(any(EventEntity.class))).thenReturn(eventEntity);
        when(file.isEmpty()).thenReturn(true);

        eventDto.setCapacity(100);
        eventDto.setDescription("Updated Event");

        EventDto result = eventService.updateEvent(eventDto, file);

        assertNotNull(result);
        assertEquals(100, result.getCapacity());
        assertEquals("Updated Event", result.getDescription());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testDeleteEvent_Success() {
        when(eventRepository.existsById(anyLong())).thenReturn(true);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));

        eventService.deleteEvent(1L);

        verify(eventRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testRegisterUserForEvent_Success() {
        eventEntity.setParticipants(new ArrayList<>());
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        eventService.registerUserForEvent(1L, 1L);

        assertTrue(eventEntity.getParticipants().contains(userEntity));
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testUnregisterUserFromEvent_Success() {
        List<UserEntity> participants = new ArrayList<>();
        participants.add(userEntity);
        eventEntity.setParticipants(participants);

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        eventService.unregisterUserFromEvent(1L, 1L);

        assertFalse(eventEntity.getParticipants().contains(userEntity));
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void testGetEventById_Success() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));

        EventDto result = eventService.getEventById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(eventRepository, times(1)).findById(anyLong());
    }
}

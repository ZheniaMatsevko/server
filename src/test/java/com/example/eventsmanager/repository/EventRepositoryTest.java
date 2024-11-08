package com.example.eventsmanager.repository;

import com.example.eventsmanager.event.EventEntity;
import com.example.eventsmanager.event.IEventRepository;
import com.example.eventsmanager.user.IUserRepository;
import com.example.eventsmanager.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class EventRepositoryTest {

    @Autowired
    private IEventRepository eventRepository;

    @Autowired
    private IUserRepository userRepository;

    private UserEntity organiser;
    private UserEntity participant;

    @BeforeEach
    void setUpData() {
        // Create and save organiser
        organiser = new UserEntity();
        organiser.setUsername("Organiser");
        organiser.setEmail("organiser@example.com");
        organiser.setFirstname("Jane");
        organiser.setLastname("Doe");
        organiser.setPassword("password");
        organiser = userRepository.saveAndFlush(organiser);

        // Create and save participant
        participant = new UserEntity();
        participant.setUsername("Participant");
        participant.setEmail("participant@example.com");
        participant.setFirstname("John");
        participant.setLastname("Smith");
        participant.setPassword("password");
        participant = userRepository.saveAndFlush(participant);

        // Create and save event
        EventEntity event1 = new EventEntity();
        event1.setCaption("Event 1");
        event1.setDateTime(LocalDateTime.now().plusDays(1));
        event1.setPrice(50.0f);
        event1.setCapacity(100);
        event1.setDescription("First Event");
        event1.setOrganiser(organiser);
        event1.setOnline(true);
        List<UserEntity> participants = new ArrayList<>();
        participants.add(participant);
        event1.setParticipants(participants);
        eventRepository.saveAndFlush(event1);

        EventEntity event2 = new EventEntity();
        event2.setCaption("Event 2");
        event2.setDateTime(LocalDateTime.now().plusDays(2));
        event2.setPrice(30.0f);
        event2.setCapacity(80);
        event2.setDescription("Second Event");
        event2.setOrganiser(organiser);
        event2.setOnline(false);
        eventRepository.saveAndFlush(event2);
    }

    @Test
    void testFindAllByOrganiserId_Found() {
        List<EventEntity> events = eventRepository.findAllByOrganiserId(organiser.getId());

        assertFalse(events.isEmpty(), "Expected to find events by organiser");
        assertEquals(2, events.size(), "Expected exactly 2 events for the organiser");
    }

    @Test
    void testFindAllByOrganiserId_NotFound() {
        List<EventEntity> events = eventRepository.findAllByOrganiserId(99L);

        assertTrue(events.isEmpty(), "Expected no events for a non-existing organiser ID");
    }

    @Test
    void testFindAllByParticipantId_Found() {
        List<EventEntity> events = eventRepository.findAllByParticipantId(participant.getId());

        assertFalse(events.isEmpty(), "Expected to find events for the participant");
        assertEquals(1, events.size(), "Expected exactly 1 event for the participant");
    }

    @Test
    void testFindAllByParticipantId_NotFound() {
        List<EventEntity> events = eventRepository.findAllByParticipantId(99L);

        assertTrue(events.isEmpty(), "Expected no events for a non-existing participant ID");
    }

    @Test
    void testSaveEvent_Success() {
        EventEntity newEvent = new EventEntity();
        newEvent.setCaption("New Event");
        newEvent.setDateTime(LocalDateTime.now().plusDays(3));
        newEvent.setPrice(20.0f);
        newEvent.setCapacity(50);
        newEvent.setDescription("Newly added event");
        newEvent.setOrganiser(organiser);
        newEvent.setOnline(false);

        EventEntity savedEvent = eventRepository.save(newEvent);
        assertNotNull(savedEvent.getId(), "Expected saved event to have an ID");
        assertEquals("New Event", savedEvent.getCaption());
    }

    @Test
    void testFindById_Found() {
        Long eventId = eventRepository.findAll().get(0).getId();
        Optional<EventEntity> foundEvent = eventRepository.findById(eventId);

        assertTrue(foundEvent.isPresent(), "Expected to find event by ID");
        assertEquals(eventId, foundEvent.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        Optional<EventEntity> foundEvent = eventRepository.findById(999L);
        assertFalse(foundEvent.isPresent(), "Expected no event to be found for a non-existing ID");
    }

    @Test
    void testDeleteById_Success() {
        Long eventId = eventRepository.findAll().get(0).getId();
        eventRepository.deleteById(eventId);

        Optional<EventEntity> deletedEvent = eventRepository.findById(eventId);
        assertFalse(deletedEvent.isPresent(), "Expected event to be deleted");
    }

    @Test
    void testFindAll_Success() {
        List<EventEntity> events = eventRepository.findAll();
        assertEquals(2, events.size(), "Expected to retrieve all events");
    }
}


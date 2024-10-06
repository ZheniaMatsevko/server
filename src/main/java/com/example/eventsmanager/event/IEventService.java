package com.example.eventsmanager.event;


import com.example.eventsmanager.user.UserDto;

import java.util.List;

public interface IEventService {
    List<EventDto> getAll();

    EventDto createEvent(EventDto event);

    EventDto updateEvent(EventDto event);

    void deleteEvent(Long eventId);

    void addParticipant(Long eventId, UserDto user);

    void removeParticipant(Long eventId, UserDto user);

    List<EventDto> findAllByOrganiserId(Long organiserId);

    List<EventDto> findAllForParticipantById(Long id);

    EventDto getEventById(Long eventId);
}

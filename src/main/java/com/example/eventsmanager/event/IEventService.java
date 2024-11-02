package com.example.eventsmanager.event;


import com.example.eventsmanager.user.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IEventService {
    List<EventDto> getAll();

    EventDto addEvent(EventDto event, MultipartFile file);

    EventDto updateEvent(EventDto event, MultipartFile file);

    void deleteEvent(Long eventId);

    void registerUserForEvent(Long userId, Long eventId);
    void unregisterUserFromEvent(Long userId, Long eventId);

    List<EventDto> findAllByOrganiserId(Long organiserId);

    List<EventDto> findAllForParticipantById(Long id);

    EventDto getEventById(Long eventId);
}

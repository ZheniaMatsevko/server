package com.example.eventsmanager.event;

import com.example.eventsmanager.user.DeletedUserEvent;
import com.example.eventsmanager.user.IUserMapper;
import com.example.eventsmanager.user.UserDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService implements IEventService{

    private final IEventRepository eventRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<EventDto> getAll() {
        return eventRepository.findAll().stream().map(IEventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    @Transactional
    public EventDto createEvent(EventDto event) {
        log.info("Creating event");
        EventEntity createdEvent = eventRepository.save(IEventMapper.INSTANCE.dtoToEntity(event));

        log.info("Event created successfully.");

        return IEventMapper.INSTANCE.entityToDto(createdEvent);
    }

    @Transactional
    public EventDto updateEvent(EventDto event) {
        Optional<EventEntity> optional = eventRepository.findById(event.getId());
        if (optional.isPresent()) {
            EventEntity eventEntity = optional.get();
            eventEntity.setCapacity(event.getCapacity());
            eventEntity.setDescription(event.getDescription());
            eventEntity.setCaption(event.getCaption());
            eventEntity.setAddress(event.getAddress());
            eventEntity.setDateTime(event.getDateTime());
            eventEntity.setPrice(event.getPrice());
            eventEntity.setOnline(event.isOnline());
            EventEntity editedEvent = eventRepository.save(eventEntity);
            log.info("Updating event with id {}", editedEvent.getId());
            return IEventMapper.INSTANCE.entityToDto(editedEvent);
        } else {
            throw new EntityNotFoundException("Event not found for editing");
        }
    }

    public void deleteEvent(Long eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            eventPublisher.publishEvent(new DeletedEventEvent(eventId));
            log.info("Deleted event with ID: {}", eventId);
        } else {
            log.warn("Event not found for deletion with ID: {}", eventId);
        }
    }

    public void addParticipant(Long eventId, UserDto user){
        EventEntity event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            log.warn("Event not found with ID: {}", eventId);
            throw new EntityNotFoundException("Event with id " + eventId + " not found");
        }

        event.getParticipants().add(IUserMapper.INSTANCE.dtoToEntity(user));
        eventRepository.save(event);
    }

    public void removeParticipant(Long eventId, UserDto user){
        EventEntity event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            log.warn("Event not found with ID: {}", eventId);
            throw new EntityNotFoundException("Event with id " + eventId + " not found");
        }

        event.getParticipants().remove(IUserMapper.INSTANCE.dtoToEntity(user));
        eventRepository.save(event);
    }

    public List<EventDto> findAllByOrganiserId(Long organiserId) {
        List<EventEntity> events = eventRepository.findAllByOrganiserId(organiserId);
        if (events.isEmpty()) {
            log.warn("No events found for organiser ID: {}", organiserId);
            return Collections.emptyList();
        }
        log.info("Retrieved {} events for organiser ID: {}", events.size(), organiserId);
        return events.stream().map(IEventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    @EventListener
    public void onDeletedUserEvent(DeletedUserEvent event) {
        List<EventEntity> organisedEvents = eventRepository.findAllByOrganiserId(event.getUserId());
        eventRepository.deleteAll(organisedEvents);

        List<EventEntity> participationEvents = eventRepository.findAllByParticipantId(event.getUserId());
        for(EventEntity eventEntity :participationEvents){
            eventEntity.getParticipants().removeIf(user -> user.getId().equals(event.getUserId()));
            eventRepository.save(eventEntity);
        }
    }

    public List<EventDto> findAllForParticipantById(Long id) {
        List<EventEntity> events = eventRepository.findAllByParticipantId(id);
        if (events.isEmpty()) {
            log.warn("No events found for participant ID: {}", id);
            return Collections.emptyList();
        }
        log.info("Retrieved {} events for participant ID: {}", events.size(), id);
        return events.stream().map(IEventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    public EventDto getEventById(Long eventId) {
        EventEntity event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            log.info("Retrieved event with ID: {}", event.getId());
        } else {
            log.warn("Event not found with ID: {}", eventId);
            throw new EntityNotFoundException("Event with id " + eventId + " not found");
        }
        return IEventMapper.INSTANCE.entityToDto(event);
    }
}

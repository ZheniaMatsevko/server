package com.example.eventsmanager.event;

import com.example.eventsmanager.event.address.IAddressMapper;
import com.example.eventsmanager.user.IUserMapper;
import com.example.eventsmanager.user.IUserRepository;
import com.example.eventsmanager.user.UserDto;
import com.example.eventsmanager.user.UserEntity;
import com.example.eventsmanager.utils.ImagesManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final IEventRepository eventRepository;
    private final IUserRepository userRepository;

    public List<EventDto> getAll() {
        return eventRepository.findAll().stream().map(IEventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto addEvent(EventDto event, MultipartFile file) {
        try {
            log.info("Creating event");
            EventEntity createdEvent = eventRepository.save(IEventMapper.INSTANCE.dtoToEntity(event));
            if(file!=null && !file.isEmpty()){
                String imagePath= ImagesManager.saveEventImage(file,event.getOrganiser().getId(),createdEvent.getId());
                eventRepository.updateImageUrl(createdEvent.getId(),imagePath);
                createdEvent.setImageUrl(imagePath);
            }
            log.info("Event created successfully.");
            return IEventMapper.INSTANCE.entityToDto(createdEvent);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public EventDto updateEvent(EventDto event, MultipartFile file) {
        Optional<EventEntity> optional = eventRepository.findById(event.getId());
        if(optional.isPresent()){
            EventEntity eventEntity = optional.get();
            eventEntity.setCapacity(event.getCapacity());
            eventEntity.setDescription(event.getDescription());
            eventEntity.setCaption(event.getCaption());
            eventEntity.setAddress(IAddressMapper.INSTANCE.dtoToEntity(event.getAddress()));
            eventEntity.setDateTime(event.getDateTime());
            eventEntity.setPrice(event.getPrice());
            eventEntity.setOnline(event.isOnline());
            EventEntity editedEvent = eventRepository.save(eventEntity);
            log.info("Updating event with id "+editedEvent.getId());
            try {
                if(file!=null && !file.isEmpty()){
                    String imagePath= ImagesManager.saveEventImage(file,event.getOrganiser().getId(),editedEvent.getId());
                    eventRepository.updateImageUrl(editedEvent.getId(),imagePath);
                    editedEvent.setImageUrl(imagePath);
                }
                log.info("Event edited successfully.");
                return IEventMapper.INSTANCE.entityToDto(editedEvent);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }else{
            throw new EntityNotFoundException("Event not found for editing");
        }
    }

    @Override
    public void deleteEvent(Long eventId) {
        try{
            if (eventRepository.existsById(eventId)) {
                Optional<EventEntity> event = eventRepository.findById(eventId);
                eventRepository.deleteById(eventId);
                log.info("Deleted event with ID: {}", eventId);
                ImagesManager.deleteImage(event.get().getImageUrl());
            } else {
                log.warn("Event not found for deletion with ID: {}", eventId);
            }
        }catch (IOException exception){
            log.error("Failed to delete event image");
        }
    }

    @Override
    public void registerUserForEvent(Long userId, Long eventId) {
        EventEntity event = eventRepository.findById(eventId).orElse(null);
        UserEntity user = userRepository.findById(userId).orElse(null);

        event.getParticipants().add(user);
        eventRepository.save(event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public void unregisterUserFromEvent(Long userId, Long eventId) {
        EventEntity event = eventRepository.findById(eventId).orElse(null);
        UserEntity user = userRepository.findById(userId).orElse(null);

        event.getParticipants().remove(user);
        eventRepository.save(event);
    }

    public void addParticipant(Long eventId, UserDto user) {
        EventEntity event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            log.warn("Event not found with ID: {}", eventId);
            throw new EntityNotFoundException("Event with id " + eventId + " not found");
        }

        event.getParticipants().add(IUserMapper.INSTANCE.dtoToEntity(user));
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

    public List<EventDto> findAllForParticipantById(Long id) {
        List<EventEntity> events = eventRepository.findAllByParticipantId(id);
        if (events.isEmpty()) {
            log.warn("No events found for participant ID: {}", id);
            return Collections.emptyList();
        }
        log.info("Retrieved {} events for participant ID: {}", events.size(), id);
        return events.stream().map(IEventMapper.INSTANCE::entityToDto).collect(Collectors.toList());
    }

    @Override
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

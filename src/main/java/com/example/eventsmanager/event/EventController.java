package com.example.eventsmanager.event;

import com.example.eventsmanager.exceptions.ExceptionHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/new")
    public EventDto createEvent(@RequestPart(value = "file", required = false) MultipartFile file,
                                       @RequestPart("event") @Valid String eventJson, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        try {
            EventDto eventDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(eventJson, EventDto.class);
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<EventDto>> violations = validator.validate(eventDto);

            if (!violations.isEmpty()) {
                String message = ExceptionHelper.formErrorMessage(violations);
                throw new javax.validation.ValidationException(message);
            }else{
                return eventService.addEvent(eventDto,file);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public EventDto updateEvent(@PathVariable Long id,@RequestPart(value = "file", required = false) MultipartFile file,
                                       @RequestPart("event") @Valid String eventJson, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        try {
            EventDto eventDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(eventJson, EventDto.class);
            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            Set<ConstraintViolation<EventDto>> violations = validator.validate(eventDto);

            if (!violations.isEmpty()) {
                String message = ExceptionHelper.formErrorMessage(violations);
                throw new javax.validation.ValidationException(message);
            }else{
                eventDto.setId(id);
                return eventService.updateEvent(eventDto,file);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        log.info("Deleting event with ID: {}", id);
        eventService.deleteEvent(id);
        log.info("Event deleted with ID: {}", id);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long eventId) {
        log.info("Retrieving event with ID: {}", eventId);
        return eventService.getEventById(eventId);
    }

    @GetMapping("/organiser/{id}")
    public List<EventDto> getAllByOrganiserId(@PathVariable Long id) {
        log.info("Getting all events by organiser id");
        return eventService.findAllByOrganiserId(id);
    }

    @GetMapping("/participant/{id}")
    public List<EventDto> getAllByParticipantId(@PathVariable Long id) {
        log.info("Getting all events by participant id");
        return eventService.findAllForParticipantById(id);
    }

    @GetMapping
    public List<EventDto> getAll() {
        log.info("Getting all events");
        return eventService.getAll();
    }

    @PutMapping("/{eventId}/register/{userId}")
    public ResponseEntity<String> registerUserForEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            eventService.registerUserForEvent(userId, eventId);
            return ResponseEntity.ok("User registered for the event successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user for the event.");
        }
    }

    @PutMapping("/{eventId}/unregister/{userId}")
    public ResponseEntity<String> unregisterUserFromEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            eventService.unregisterUserFromEvent(userId, eventId);
            return ResponseEntity.ok("User unregistered from the event successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unregister user from the event.");
        }
    }
}

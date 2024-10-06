package com.example.eventsmanager.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping()
    public EventRequestDto createEvent(@RequestBody @Valid EventRequestDto event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().toString());
        }
        EventDto createdEvent = eventService.createEvent(IEventMapper.INSTANCE.requestDtoToDto(event));
        log.info("Event created with ID: {}", createdEvent.getId());
        return IEventMapper.INSTANCE.dtoToRequestDto(createdEvent);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        log.info("Deleting event with ID: {}", id);
        eventService.deleteEvent(id);
        log.info("Event deleted with ID: {}", id);
    }

    @GetMapping("/{eventId}")
    public EventRequestDto getEventById(@PathVariable Long eventId) {
        log.info("Retrieving event with ID: {}", eventId);
        return IEventMapper.INSTANCE.dtoToRequestDto(eventService.getEventById(eventId));
    }

    @GetMapping("/organiser/{id}")
    public List<EventRequestDto> getAllByOrganiserId(@PathVariable Long id) {
        log.info("Getting all events by organiser id");
        return eventService.findAllByOrganiserId(id).stream().map(IEventMapper.INSTANCE::dtoToRequestDto).collect(Collectors.toList());
    }

    @GetMapping("/participant/{id}")
    public List<EventRequestDto> getAllByParticipantId(@PathVariable Long id) {
        log.info("Getting all events by participant id");
        return eventService.findAllForParticipantById(id).stream().map(IEventMapper.INSTANCE::dtoToRequestDto).collect(Collectors.toList());
    }

    @GetMapping
    public List<EventRequestDto> getAll() {
        log.info("Getting all events");
        return eventService.getAll().stream().map(IEventMapper.INSTANCE::dtoToRequestDto).collect(Collectors.toList());
    }
}

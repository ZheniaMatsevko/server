package com.example.eventsmanager.booking;

import com.example.eventsmanager.event.EventService;
import com.example.eventsmanager.user.UserDto;
import com.example.eventsmanager.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final EventService eventService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void registerUserForEvent(Long userId, Long eventId) {
        Optional<UserDto> user = userService.getUserById(userId);

        if (user.isEmpty()) {
            log.error("User with id {} not found", userId);
            return;
        }

        eventService.addParticipant(eventId, user.get());
        log.info("SUCCESS: User with id {} registered for event {}", userId, eventId);
    }

    @Transactional
    public void unregisterUserFromEvent(Long userId, Long eventId) {
        Optional<UserDto> user = userService.getUserById(userId);

        if (user.isEmpty()) {
            log.error("User with id {} not found", userId);
            return;
        }

        eventService.removeParticipant(eventId, user.get());
    }
}

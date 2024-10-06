package com.example.eventsmanager.booking;

import org.springframework.http.ResponseEntity;

public interface IBookingService {
    ResponseEntity<String> registerUserForEvent(Long userId, Long eventId);
    ResponseEntity<String> unregisterUserFromEvent(Long userId, Long eventId);
}

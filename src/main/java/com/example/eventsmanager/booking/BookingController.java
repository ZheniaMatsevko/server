package com.example.eventsmanager.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookingController implements IBookingService{
    private final BookingService bookingService;

    @PutMapping("/{eventId}/register/{userId}")
    public ResponseEntity<String> registerUserForEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            bookingService.registerUserForEvent(userId, eventId);
            return ResponseEntity.ok("User registered for the event successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user for the event.");
        }
    }

    @PutMapping("/{eventId}/unregister/{userId}")
    public ResponseEntity<String> unregisterUserFromEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        try {
            bookingService.unregisterUserFromEvent(userId, eventId);
            return ResponseEntity.ok("User unregistered from the event successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unregister user from the event.");
        }
    }

}

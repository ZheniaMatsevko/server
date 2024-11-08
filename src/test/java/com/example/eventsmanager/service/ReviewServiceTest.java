package com.example.eventsmanager.service;

import com.example.eventsmanager.event.EventDto;
import com.example.eventsmanager.event.EventEntity;
import com.example.eventsmanager.event.IEventRepository;
import com.example.eventsmanager.review.IReviewRepository;
import com.example.eventsmanager.review.ReviewDto;
import com.example.eventsmanager.review.ReviewEntity;
import com.example.eventsmanager.review.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private IReviewRepository reviewRepository;

    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddReview_Success() {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setComment("Great event!");

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setId(1L);

        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(1L);
        eventEntity.setReviews(new HashSet<>());

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(reviewEntity);

        ReviewDto addedReview = reviewService.addReview(reviewDto, 1L);

        assertNotNull(addedReview);
        assertEquals(1L, addedReview.getId());
        verify(eventRepository).save(eventEntity);
        verify(reviewRepository).save(any(ReviewEntity.class));
        assertTrue(eventEntity.getReviews().contains(reviewEntity));
    }

    @Test
    void testAddReview_EventNotFound() {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setComment("Nice event");

        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> reviewService.addReview(reviewDto, 1L));
        assertEquals("event not found with ID: 1", exception.getMessage());
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }

    @Test
    void testRemoveReview_Success() {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setId(1L);

        EventEntity eventEntity = new EventEntity();
        eventEntity.setId(1L);
        HashSet<ReviewEntity> reviews = new HashSet<>();
        reviews.add(reviewEntity);
        eventEntity.setReviews(reviews);

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(eventEntity));
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(reviewEntity));

        EventDto updatedEvent = reviewService.removeReview(1L, 1L);

        assertNotNull(updatedEvent);
        verify(eventRepository).save(eventEntity);
        verify(reviewRepository).deleteById(1L);
        assertFalse(eventEntity.getReviews().contains(reviewEntity));
    }

    @Test
    void testRemoveReview_EventNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> reviewService.removeReview(1L, 1L));
        assertEquals("Event not found with ID: 1", exception.getMessage());
        verify(reviewRepository, never()).deleteById(anyLong());
    }
}

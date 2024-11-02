package com.example.eventsmanager.review;

import com.example.eventsmanager.event.EventDto;

import java.util.List;

public interface IReviewService {
    ReviewDto addReview(ReviewDto reviewDto, Long eventId);
    EventDto removeReview(Long reviewId, Long eventId);
}

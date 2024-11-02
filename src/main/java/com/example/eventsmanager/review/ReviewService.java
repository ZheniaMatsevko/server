package com.example.eventsmanager.review;

import com.example.eventsmanager.event.EventDto;
import com.example.eventsmanager.event.EventEntity;
import com.example.eventsmanager.event.IEventMapper;
import com.example.eventsmanager.event.IEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IEventRepository eventRepository;
    @Override
    public ReviewDto addReview(ReviewDto reviewDto, Long eventId) {
        Optional<EventEntity> optional = eventRepository.findById(eventId);
        if (optional.isPresent()) {
            EventEntity event = optional.get();
            ReviewEntity reviewEntity = IReviewMapper.INSTANCE.dtoToEntity(reviewDto);
            event.getReviews().add(reviewEntity);
            eventRepository.save(event);
            log.info("Added review to event with ID: {}", eventId);
            // Set the generated ID to the ReviewDto and return it
            reviewDto.setId(reviewEntity.getId());
            return reviewDto;
        } else {
            log.warn("Event not found for adding review to event with ID: {}", eventId);
            throw new IllegalArgumentException("event not found with ID: " + eventId);
        }
    }

    @Override
    public EventDto removeReview(Long reviewId, Long eventId) {
        Optional<EventEntity> optional = eventRepository.findById(eventId);
        if (optional.isPresent()) {
            EventEntity event  = optional.get();
            event.getReviews().remove(reviewRepository.findById(reviewId).orElse(null));
            eventRepository.save(event);
            log.info("Removed review from event with ID: {}", event);
            return IEventMapper.INSTANCE.entityToDto(event);
        } else {
            log.warn("Event not found for removing review to event with ID: {}", eventId);
            throw new IllegalArgumentException("Event not found with ID: " + eventId);
        }
    }
}

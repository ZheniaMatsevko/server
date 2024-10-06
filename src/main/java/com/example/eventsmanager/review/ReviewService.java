package com.example.eventsmanager.review;

import com.example.eventsmanager.event.DeletedEventEvent;
import com.example.eventsmanager.user.DeletedUserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ReviewResponseDto getReviewById(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));
        return IReviewMapper.INSTANCE.entityToResponseDto(review);
    }

    @Override
    public void deleteReviewById(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            log.info("Review with ID: {} has been deleted successfully", reviewId);
        } else {
            log.warn("Review not found for deletion with ID: {}", reviewId);
        }
    }

    @Override
    @Transactional
    public ReviewResponseDto createReview(CreateReviewRequestDto reviewRequestDto) {
        ReviewEntity savedReview = reviewRepository.save(IReviewMapper.INSTANCE.createRequestDtoToToEntity(reviewRequestDto));
        log.info("Review with ID: {} has been created successfully", savedReview.getId());


        return IReviewMapper.INSTANCE.entityToResponseDto(savedReview);
    }

    @Override
    public List<ReviewResponseDto> getAllReviews() {
        List<ReviewEntity> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(IReviewMapper.INSTANCE::entityToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDto> getAllReviewsByAuthorId(Long authorId) {
        List<ReviewEntity> reviews = reviewRepository.findAllByAuthorId(authorId);
        return reviews.stream()
                .map(IReviewMapper.INSTANCE::entityToResponseDto)
                .collect(Collectors.toList());
    }

    @EventListener
    public void onDeletedUserEvent(DeletedUserEvent event) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByAuthorId(event.getUserId());
        reviewRepository.deleteAll(reviewEntities);
    }

    @EventListener
    public void onDeletedEventEvent(DeletedEventEvent event) {
        List<ReviewEntity> reviewEntities = reviewRepository.findAllByEventId(event.getEventId());
        reviewRepository.deleteAll(reviewEntities);
    }
}

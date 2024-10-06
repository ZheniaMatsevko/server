package com.example.eventsmanager.review;

import java.util.List;

public interface IReviewService {
    ReviewResponseDto getReviewById(Long reviewId);
    void deleteReviewById(Long reviewId);
    ReviewResponseDto createReview(CreateReviewRequestDto reviewRequestDto);
    List<ReviewResponseDto> getAllReviews();
    List<ReviewResponseDto> getAllReviewsByAuthorId(Long authorId);
}

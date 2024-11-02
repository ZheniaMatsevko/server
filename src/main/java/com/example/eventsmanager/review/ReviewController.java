package com.example.eventsmanager.review;

import com.example.eventsmanager.exceptions.ExceptionHelper;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final IReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{eventId}")
    public ReviewRequestDto addReview(@PathVariable Long eventId, @RequestBody @Valid ReviewRequestDto reviewRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ExceptionHelper.formErrorMessage(bindingResult);
            throw new ValidationException(message);
        }
        ReviewDto reviewDto = reviewService.addReview(IReviewMapper.INSTANCE.requestDtoToDto(reviewRequestDto),eventId);
        log.info("Reviews added to event with ID: {}", eventId);
        return IReviewMapper.INSTANCE.dtoToRequestDto(reviewDto);

    }

    @DeleteMapping("/{reviewId}/{eventId}")
    public void deleteReview(@PathVariable Long reviewId, @PathVariable Long eventId) {
        reviewService.removeReview(reviewId,eventId);
        log.info("Review removed from event with ID: {}", eventId);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String errorMessage = "ERROR: " + e.getMessage();
        log.error(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

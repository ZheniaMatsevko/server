package com.example.eventsmanager.repository;

import com.example.eventsmanager.review.IReviewRepository;
import com.example.eventsmanager.review.ReviewEntity;
import com.example.eventsmanager.user.IUserRepository;
import com.example.eventsmanager.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private IUserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setUsername("Author1");
        user1.setEmail("email@gmail.com");
        user1.setFirstname("John");
        user1.setLastname("Doe");
        user1.setPassword("Password1");
        user1 = userRepository.saveAndFlush(user1);

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setUsername("Author2");
        user2.setEmail("email2@gmail.com");
        user2.setFirstname("John");
        user2.setLastname("Doe");
        user2.setPassword("Password1");
        user2 = userRepository.saveAndFlush(user2);

        // Create and save sample reviews
        ReviewEntity review1 = new ReviewEntity();
        review1.setAuthor(user1);
        review1.setRating(2);
        review1.setComment("Review 1");
        reviewRepository.saveAndFlush(review1);

        ReviewEntity review2 = new ReviewEntity();
        review2.setAuthor(user1);
        review2.setRating(3);
        review2.setComment("Review 2");
        reviewRepository.saveAndFlush(review2);

        ReviewEntity review3 = new ReviewEntity();
        review3.setAuthor(user2);
        review3.setRating(4);
        review3.setComment("Review 3");
        reviewRepository.saveAndFlush(review3);
    }

    @Test
    void testFindAllByAuthorId_NotFound() {
        // Author with ID 99 has no reviews in test data
        List<ReviewEntity> reviews = reviewRepository.findAllByAuthorId(99L);
        assertTrue(reviews.isEmpty(), "Expected no reviews for author with ID 99");
    }

    @Test
    void testSaveReview_Success() {
        ReviewEntity newReview = new ReviewEntity();
        newReview.setComment("Great event!");
        newReview.setAuthor(userRepository.findById(1L).orElse(null)); // Use existing author

        ReviewEntity savedReview = reviewRepository.save(newReview);

        assertNotNull(savedReview.getId(), "Expected saved review to have an ID");
        assertEquals("Great event!", savedReview.getComment());
    }

    @Test
    void testDeleteReview_Success() {
        reviewRepository.deleteById(1L);

        assertFalse(reviewRepository.findById(1L).isPresent(), "Expected review with ID 1 to be deleted");
    }
}

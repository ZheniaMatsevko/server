package com.example.eventsmanager.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findAllByAuthorId(Long authorId);
}

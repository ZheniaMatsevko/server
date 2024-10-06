package com.example.eventsmanager.review;

import com.example.eventsmanager.event.EventEntity;
import com.example.eventsmanager.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EventEntity event;

    @ManyToOne
    private UserEntity author;

    private int rating;

    @Column(length = 300)
    private String comment;
}

package com.example.eventsmanager.event;

import com.example.eventsmanager.review.ReviewEntity;
import com.example.eventsmanager.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String caption;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    private String address;

    @Column(nullable = false)
    private float price;

    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ReviewEntity> reviews;

    private int capacity;
    private String imageUrl;
    private String description;

    private boolean online;
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "participant_id", nullable = false),
            uniqueConstraints = @UniqueConstraint(columnNames = {"participant_id", "event_id"})
    )
    private List<UserEntity> participants;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organiser_id", nullable = false)
    private UserEntity organiser;
}

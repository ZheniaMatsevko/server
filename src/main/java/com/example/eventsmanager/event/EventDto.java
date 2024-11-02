package com.example.eventsmanager.event;

import com.example.eventsmanager.review.ReviewDto;
import com.example.eventsmanager.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String caption;
    private LocalDateTime dateTime;
    private float price;
    @EqualsAndHashCode.Exclude
    private List<UserDto> participants;
    private String description;
    private String imageUrl;
    private boolean online;
    private int capacity;
    private String address;
    private UserDto organiser;
    @EqualsAndHashCode.Exclude
    private Set<ReviewDto> reviews;

}

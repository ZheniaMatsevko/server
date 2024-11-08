package com.example.eventsmanager.event;

import com.example.eventsmanager.event.address.AddressDto;
import com.example.eventsmanager.review.ReviewDto;
import com.example.eventsmanager.user.UserDto;
import lombok.*;

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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<UserDto> participants;
    private String description;
    private String imageUrl;
    private boolean online;
    private int capacity;
    private AddressDto address;
    private UserDto organiser;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ReviewDto> reviews;

}

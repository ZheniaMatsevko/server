package com.example.eventsmanager.event;

import com.example.eventsmanager.event.address.AddressDto;
import com.example.eventsmanager.review.ReviewDto;
import com.example.eventsmanager.user.UserDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    @NotBlank
    @Size(max = 100, message = "Event caption length must be less than 100 characters")
    private String caption;
    private LocalDateTime dateTime;
    @Min(value = 0, message = "Event price must be 0 or more")
    private float price;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<UserDto> participants;
    private String description;
    private String imageUrl;
    private boolean online;
    @Min(value = 1, message = "Event capacity must be 1 or more")
    private int capacity;
    private AddressDto address;
    @NotNull
    private UserDto organiser;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<ReviewDto> reviews;

}

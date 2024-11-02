package com.example.eventsmanager.event;

import com.example.eventsmanager.review.ReviewRequestDto;
import com.example.eventsmanager.user.UserDto;
import com.example.eventsmanager.user.UserRequestDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class EventRequestDto {
    private Long id;
    private String imageUrl;
    @EqualsAndHashCode.Exclude
    private List<UserRequestDto> participants;
    @NotBlank
    @Size(max = 100, message = "Event caption length must be less than 100 characters")
    private String caption;

    @EqualsAndHashCode.Exclude
    private Set<ReviewRequestDto> reviews;
    private LocalDateTime dateTime;

    @Min(value = 0, message = "Event price must be 0 or more")
    private float price;

    @NotBlank
    private String description;

    private boolean online;

    @Min(value = 1, message = "Event capacity must be 1 or more")
    private int capacity;

    @NotBlank
    private String address;

    @NotNull
    private UserRequestDto organiser;
}

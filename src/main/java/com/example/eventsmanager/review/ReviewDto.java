package com.example.eventsmanager.review;

import com.example.eventsmanager.event.EventDto;
import com.example.eventsmanager.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private UserDto author;
    private int rating;
    private String comment;
}

package com.example.eventsmanager.review;

import com.example.eventsmanager.event.EventDto;
import com.example.eventsmanager.user.UserDto;
import com.example.eventsmanager.user.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Long id;
    private UserRequestDto author;
    private int rating;
    private String comment;
}

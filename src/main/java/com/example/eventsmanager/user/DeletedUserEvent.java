package com.example.eventsmanager.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletedUserEvent {
    private Long userId;
}

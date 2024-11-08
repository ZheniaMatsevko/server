package com.example.eventsmanager.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserUpdateDto {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String username;
}

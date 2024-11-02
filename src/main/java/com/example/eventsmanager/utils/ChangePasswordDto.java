package com.example.eventsmanager.utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChangePasswordDto {
    private Long id;
    private String oldPassword;
    private String newPassword;
}

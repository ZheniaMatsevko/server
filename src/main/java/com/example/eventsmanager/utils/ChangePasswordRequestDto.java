package com.example.eventsmanager.utils;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ChangePasswordRequestDto {
    private Long id;
    private String oldPassword;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Password need to be minimum eight characters, at least one uppercase letter, one lowercase letter and one number")
    private String newPassword;
}

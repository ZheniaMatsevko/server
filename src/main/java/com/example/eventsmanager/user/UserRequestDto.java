package com.example.eventsmanager.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 30, message = "Username length must be between 3 and 30 characters")
    private String username;

    @NotBlank
    @Size(min = 6, message = "Password length must be more than 6 characters")
    private String password;

    @Email
    private String email;

    @NotBlank
    @Size(max = 50, message = "Firstname length must be less than 50 characters")
    private String firstname;

    @NotBlank
    @Size(max = 50, message = "Lastname length must be less than 50 characters")
    private String lastname;

    private String profileImageUrl;
}

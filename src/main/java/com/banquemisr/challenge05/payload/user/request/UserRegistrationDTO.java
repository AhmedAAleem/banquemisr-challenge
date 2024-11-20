package com.banquemisr.challenge05.payload.user.request;

import com.banquemisr.challenge05.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationDTO {

    @NotBlank(message = "Username is required.")
    @Size(min = 4, max = 50, message = "Invalid, Username must be between 4 and 50 characters.")
    private String userName;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Invalid, password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format. Please enter a valid email address.")
    private String email;

    @NotNull(message = "Role is required")
    private Role role;
}

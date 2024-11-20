package com.banquemisr.challenge05.payload.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginDTO {

    @NotBlank(message = "Username is required.")
    @Size(min = 4, max = 50, message = "Invalid, Username must be between 4 and 50 characters.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Invalid, password must be at least 8 characters long.")
    private String password;
}

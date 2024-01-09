package ua.shortener.security.auth.dto.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 16, message = "Username must be at least 3 characters long")

    String name;@NotBlank(message = "Password is required")
    @Size(min = 6, max = 24, message = "Password must be at least 6 characters long")
    String password;@Email(message = "Invalid email format")

    @NotBlank(message = "Email is required")
    String email;
}

package ua.shortener.security.auth.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 16, message = "Username must be at least 3 characters long")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 24, message = "Password must be at least 6 characters long")
    private String password;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}

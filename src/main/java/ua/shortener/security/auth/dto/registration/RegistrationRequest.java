package ua.shortener.security.auth.dto.registration;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String name;
    private String password;
    private String email;
}

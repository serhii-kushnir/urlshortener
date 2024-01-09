package ua.shortener.security.auth.dto.registration;

import lombok.Data;

@Data
public class RegistrationRequest {

    String name;
    String password;
    String email;
}

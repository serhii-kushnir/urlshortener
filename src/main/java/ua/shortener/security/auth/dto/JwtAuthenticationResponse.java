package ua.shortener.security.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtAuthenticationResponse {
    private int status;
    private String message;
}

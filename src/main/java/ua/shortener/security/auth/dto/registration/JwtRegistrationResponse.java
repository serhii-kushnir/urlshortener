package ua.shortener.security.auth.dto.registration;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class JwtRegistrationResponse {
    private int status;
    private List<ResponseRegisterError> errors;
    private List<String> messages;
}

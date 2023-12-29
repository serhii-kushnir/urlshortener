package ua.shortener.security.auth.dto.login;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class JwtLoginResponse {
    private int status;
    private List<ResponseLoginError> errors;
    private List<String> messages;
}

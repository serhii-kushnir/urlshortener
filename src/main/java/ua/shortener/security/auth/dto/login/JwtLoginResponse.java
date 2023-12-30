package ua.shortener.security.auth.dto.login;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class JwtLoginResponse {
    private int status;
    private Map<ResponseLoginError, String> errors;

}
